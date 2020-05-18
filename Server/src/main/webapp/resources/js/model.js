class Model {
    _posts;

    constructor() {
        this._posts = [];

        let values = [];
        let keys = Object.keys(localStorage);
        let i = keys.length;

        while (i--) {
            if (Number.parseInt(keys[i])) {
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
        hashTags: val => typeof val === 'string' && (val.length > 0 && val.includes('#') || val.length === 0),
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

        return this.validateForm(post);
    }

    validateForm(form) {
        let errors = Object.keys(form)
            .filter(key => !this._postSchema[key]?.(form[key]))
            .map(key => new Error(key + ' is invalid!'));

        if (errors.length > 0) {
            alert(errors.map(error => error.message).join(" "));
            return false;
        }

        return true;
    }

    async addPost(post) {
        post.id = this._posts.length === 0 ? 1 : this._posts[this._posts.length - 1].id + 1;

        post.likes = [];
        post.createdAt = new Date();
        post.author = localStorage.getItem('user');
        post.hashTags = post.hashTags.split("#").filter(tag => tag !== '');

        return await fetch('tweets', {
            method: 'Post',
            body: JSON.stringify({
                photoLink: post.photoLink,
                description: post.description,
                hashTags: post.hashTags,
                authorId: localStorage.getItem('userId'),
                createdAt: post.createdAt.toLocaleString('en-US')
            })
        })
    }

    async changeLikes(post) {
        return await fetch('/tweets/like',{
            method: 'Post',
            body: JSON.stringify({
                twitId: post.id,
                userId: localStorage.getItem('userId')
            })
        });
    }

    async removePost(id) {
        return await fetch('/tweets?id=' + id, {
            method: 'Delete'
        })
    }

    async getPost(id) {
        return await fetch('/tweets?id=' + id, {
            method: 'Get'
        })
    }

    async getPage(filterConfig = {}) {
        if(filterConfig.dateFrom) {
            filterConfig.dateFrom = filterConfig.dateFrom.toLocaleString('en-US');
        }
        if(filterConfig.dateTo) {
            filterConfig.dateTo = filterConfig.dateTo.toLocaleString('en-US');
        }

        return await fetch('/tweets/search', {
            method: 'Post',
            body: JSON.stringify(filterConfig)
        })
    }

    async findUserByName(name) {
        return await fetch('/user?name='+ name, {
            method: 'Get'
        })
    }

    async editPost(id, form) {
        form.hashTags = form.hashTags.split("#").filter(tag => tag !== '');

        return await fetch('/tweets', {
            method: 'Put',
            body: JSON.stringify({
                id: id,
                description: form.description,
                photoLink: form.photoLink,
                hashTags: form.hashTags
            })
        });
    }
}