class Model {

    _baseUrl = 'http://localhost:8080/Server_war';

    constructor() {}

    _postSchema = {
        id: val => typeof val === 'number',
        description: val => typeof val === 'string' && val.length < 200,
        createdAt: val => Object.prototype.toString.call(val) === '[object Date]',
        author: val => typeof val === 'string' && val.length > 0,
        photoLink: val => typeof val === 'string',
        hashTags: val => typeof val === 'string' && (val.length > 0 && val.includes('#') || val.length === 0),
        likes: val => Array.isArray(val)
    };

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
        post.likes = [];
        post.author = localStorage.getItem('user');
        post.hashTags = post.hashTags.split("#").filter(tag => tag !== '');

        console.log(JSON.stringify({
            photoLink: post.photoLink,
            description: post.description,
            hashTags: post.hashTags,
            authorId: localStorage.getItem('userId')
        }));

        return await (await fetch(this._baseUrl + '/tweets', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({
                photoLink: post.photoLink,
                description: post.description,
                hashTags: post.hashTags,
                authorId: localStorage.getItem('userId')
            })
        })).json();
    }

    async changeLikes(id) {
        return await (await fetch(this._baseUrl + '/tweets/like', {
            method: 'POST',
            body: JSON.stringify({
                twitId: id,
                userId: localStorage.getItem('userId')
            }),
            headers: {
                'Content-Type': 'application/json',
            }
        })).text();
    }

    async removePost(id) {
        return await fetch(this._baseUrl + '/tweets?id=' + id, {
            method: 'Delete'
        });
    }

    async getPost(id) {
        return await (await fetch(this._baseUrl + '/tweets?id=' + id, {
            method: 'Get'
        })).json();
    }

    async getPage(filterConfig = {}) {
        const posts = await (await fetch(this._baseUrl + '/tweets/search', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(filterConfig)
        })).json();

        posts.forEach(post => post.createdAt = new Date(post.createdAt));

        return posts;
    }

    async findUserByName(name) {
        return await (await fetch(this._baseUrl + '/user?name=' + name, {
            method: 'Get'
        })).json();
    }

    async editPost(id, form) {
        form.hashTags = form.hashTags.split("#").filter(tag => tag !== '');

        return await (await fetch(this._baseUrl + '/tweets', {
            method: 'Put',
            body: JSON.stringify({
                id: id,
                description: form.description,
                photoLink: form.photoLink,
                hashTags: form.hashTags
            })
        })).json();
    }
}