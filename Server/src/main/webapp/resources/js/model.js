class Model {
    _posts;

    constructor() {
        this._posts = [];

        let values = [];
        let keys = Object.keys(localStorage);
        let i = keys.length;

        while ( i-- ) {
            if(Number.parseInt(keys[i])) {
                let value = JSON.parse(localStorage.getItem(keys[i]));
                value.createdAt = new Date(value.createdAt);
                values.push(value);
            }
        }

        this._posts = values.sort((a, b) => a.id - b.id);
    }

    _postSchema = {
        id: val => typeof val === 'number',
        description: val => typeof val === 'string' && val.length < 200,
        createdAt: val => Object.prototype.toString.call(val) === '[object Date]',
        author: val => typeof val === 'string' && val.length > 0,
        photoLink: val => typeof val === 'string',
        hashTags: val => typeof val === 'string' && val.length > 0 && val.includes('#'),
        likes: val => Array.isArray(val)
    };

    validatePost(post) {
        if (this._posts.find(p => post.id === p.id) != null) {
            console.log('this id already exist');
            return false;
        }

        if ((Math.abs(Object.keys(post).length - Object.keys(this._postSchema).length) === 1 && post['photoLink'] != null)
            || Math.abs(Object.keys(post).length - Object.keys(this._postSchema).length) > 1) {
            console.log('illegal arguments number');
            return false;
        }

        return this._validateForm(post);
    }

    _validateForm(form) {
        let errors = Object.keys(form)
            .filter(key => !this._postSchema[key]?.(form[key]))
            .map(key => new Error(key + ' is invalid!'));

        if (errors.length > 0) {
            alert(errors.map(error => error.message).join(" "));
            return false;
        }

        return true;
    }

    addPost(post) {
        if (this._posts.length === 0) {
            post.id = 1;
        } else {
            post.id = this._posts[this._posts.length - 1].id + 1;
        }

        post.likes = [];
        post.createdAt = new Date();
        post.author = localStorage.getItem('user');

        if (this.validatePost(post)) {
            post.hashTags = post.hashTags.split("#").filter(tag => tag !== '');
            this._posts.push(post);
            localStorage.setItem(post.id, JSON.stringify(post));

            return true;
        }

        return false;
    }

    changeLikes(post) {
        const user = localStorage.getItem('user');
        const startLength = post.likes.length;

        post.likes = post.likes.filter(likeOwner => likeOwner !== user);

        if(startLength === post.likes.length) {
            post.likes.push(user);
        }

        localStorage.setItem(post.id, JSON.stringify(post));
    }

    removePost(id) {
        let startLength = this._posts.length;
        this._posts = this._posts.filter(post => post.id !== id);

        if(startLength !== this._posts.length) {
            localStorage.removeItem(id);
            return true;
        }

        return false;
    }

    getPost(id) {
        return this._posts.find(post => post.id === id);
    }

    getPage(skip = 0, top = 10, filterConfig) {
        if (filterConfig != null) {
            return this._posts.filter(p => (filterConfig.author == null || filterConfig.author.length === 0 || p.author === filterConfig.author) &&
                (isNaN(filterConfig.dateFrom) || p.createdAt > filterConfig.dateFrom) &&
                (isNaN(filterConfig.dateTo) || p.createdAt <= filterConfig.dateTo) &&
                (filterConfig.hashTags == null || filterConfig.hashTags.filter(tag => p.hashTags.includes(tag)).length === filterConfig.hashTags.length))
                .slice(skip, skip + top).sort((a, b) => a.createdAt - b.createdAt);
        }
        return this._posts.slice(skip, skip + top).sort(a => a.createdAt.getDate());
    }

    editPost(id, form) {
        let postToEdit = this._posts.find(post => post.id === id);

        if (form != null && form.id == null && form.author == null && this._validateForm(form)) {
            form.hashTags = form.hashTags.split("#").filter(tag => tag !== '');
            Object.keys(form).forEach(key => {
                postToEdit[key] = form[key];
            });
            localStorage.setItem(id, JSON.stringify(postToEdit));

            return true;
        }

        return false;
    }

    addAll(posts) {
        return posts.filter(post => !this.addPost(post));
    }

    clear() {
        this._posts = [];
    }
}