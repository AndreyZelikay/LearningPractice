let posts = [
    {
        id: '1',
        description: 'test1',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Какой-то пользователь',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'hash', 'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    },
    {
        id: '2',
        description: 'test2',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Andrey',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'hash', 'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    },
    {
        id: '3',
        description: 'test3',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Какой-то пользователь',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'hash', 'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    },
    {
        id: '4',
        description: 'test4',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Andrey',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'hash', 'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    },
    {
        id: '5',
        description: 'test5',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Какой-то пользователь',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'hash', 'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    },
    {
        id: '6',
        description: 'test6',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Какой-то пользователь',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'hash', 'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    },
    {
        id: '7',
        description: 'test7',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Andrey',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'hash', 'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    },
    {
        id: '8',
        description: 'test8',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Какой-то пользователь',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'hash', 'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    },
    {
        id: '9',
        description: 'test9',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Какой-то пользователь',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    },
    {
        id: '10',
        description: 'test10',
        createdAt: new Date('2020-03-17T23:00:00'),
        author: 'Какой-то пользователь',
        photoLink: 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg',
        hashTags: [
            'tag'
        ],
        likes: [
            'Другой пользователь'
        ]
    }
]

class Model {

    _posts;

    constructor(posts) {
        this._posts = [];
        this.addAll(posts);
    }

    _postSchema = {
        id: val => typeof val === 'string' && val.length > 0,
        description: val => typeof val === 'string' && val.length < 200,
        createdAt: val => Object.prototype.toString.call(val) === '[object Date]',
        author: val => typeof val === 'string' && val.length > 0,
        photoLink: val => typeof val === 'string',
        hashTags: val => Array.isArray(val),
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
            errors.forEach(error => console.log(error.message));
            return false;
        }

        return true;
    }

    addPost(post) {
        if (this.validatePost(post)) {
            this._posts.push(post);
            return true;
        }

        return false;
    }

    removePost(id) {
        let startLength = this._posts.length;
        this._posts = this._posts.filter(post => post.id !== id);

        return startLength !== this._posts.length;
    }

    getPost(id) {
        return this._posts.find(post => post.id === id);
    }

    getPage(skip = 0, top = 10, filterConfig) {
        if (filterConfig != null) {
            return this._posts.filter(p => (filterConfig['author'] == null || p.author === filterConfig['author']) &&
                (filterConfig['fromDate'] == null || p.createdAt > filterConfig['fromDate']) &&
                (filterConfig['toDate'] == null || p.createdAt <= filterConfig['toDate']) &&
                (filterConfig['tags'] == null || filterConfig['tags'].filter(tag => p.hashTags.includes(tag)).length === filterConfig['tags'].length))
                .slice(skip, skip + top).sort(a => a.createdAt.getDate());
        }

        return this._posts.slice(skip, skip + top).sort(a => a.createdAt.getDate());
    }

    editPost(id, form) {
        let postToEdit = this._posts.find(post => post.id === id);

        if (form != null && form.id == null && form.author == null && this._validateForm(form)) {
            Object.keys(form).forEach(key => {
                postToEdit[key] = form[key];
            });
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

class PostView {
    _currentUser = 'Andrey';
    _postTemplate = document.getElementById('twit-template');
    _postContainer = document.getElementById('container');

    _fillPostData(postTemplate, data) {
        if(this._currentUser !== data.author) {
            postTemplate.querySelector('[class = "twit-buttons"]').setAttribute('style', 'display: none')
        }
        postTemplate.firstElementChild.setAttribute('id', data.id);
        postTemplate.querySelector('[data-target = "user-icon"]').setAttribute('src', data.photoLink);
        postTemplate.querySelector('[data-target = "description"]').textContent = data.description;
        postTemplate.querySelector('[data-target = "photoLink"]').setAttribute('src', data.photoLink);
        postTemplate.querySelector('[data-target = "createdAt"]').textContent = data.createdAt.toLocaleDateString('en-US');
        postTemplate.querySelector('[data-target = "author"]').textContent = data.author;
        postTemplate.querySelector('[data-target = "hashTags"]').textContent = String(data.hashTags.map(item => '#' + item));
        postTemplate.querySelector('[data-target = "likes"]').textContent = String(data.likes.length);
    }

    displayPost(post) {
        let postNode = document.importNode(this._postTemplate.content, true);

        this._fillPostData(postNode, post);

        this._postContainer.insertBefore(postNode, this._postContainer.lastElementChild);
    }

    refactorPost(id, post) {
        let postNode = document.importNode(this._postTemplate.content, true);

        this._fillPostData(postNode, post);

        document.getElementById(id).replaceWith(postNode);
    }

    removePost(id) {
        document.getElementById(id)?.remove();
    }

    clearList() {
        let first = this._postContainer.firstElementChild;
        while (first && first !== this._postContainer.lastElementChild) {
            first.remove();
            first = this._postContainer.firstElementChild;
        }
    }

    displayPosts(posts) {
        posts.forEach(post => this.displayPost(post));
    }
}

class HeaderView {
    _header = document.getElementById("header");
    _filtration = document.getElementById("filtration");

    displayCurrentUser(userName) {
        this._header.querySelector('[class = "my-profile"]').textContent = userName;
    }

    fillForm(author, dateFrom, dateUntil) {
        document.getElementById('userName').setAttribute('placeholder', author);
        document.getElementById('dateFrom').setAttribute('placeholder', dateFrom.toLocaleDateString('en-US'));
        document.getElementById('dateUntil').setAttribute('placeholder', dateUntil.toLocaleDateString('en-US'));
    }

    _fillPopUpMenu(menu, data) {
        data.forEach(element => {
            let option = document.importNode(document.getElementById('option'), true);
            option.textContent = element;
            option.setAttribute('class','select-option')
            menu.appendChild(option);
        });
    }

    displayTagSelect(tags) {
        let menu = document.getElementById('tagSelect');

        this.clearSelector(menu);

        this._fillPopUpMenu(menu, tags);
    }

    displayUsersSelect(users) {
        let menu = document.getElementById('userSelect');

        this._fillPopUpMenu(menu, users);
    }

    clearSelector(selector) {
        let last = selector.lastElementChild;
        while (last && last !== selector.firstElementChild) {
            last.remove();
            last = selector.lastElementChild;
        }
    }
}

let model;
let view;

window.onload = () => {
    let headerView = new HeaderView();
    document.addEventListener("click", function () {
        headerView.clearSelector(document.getElementById('tagSelect'));
    })
    headerView.displayCurrentUser("Andrey");
    headerView.fillForm('Andrey', new Date(), new Date());
    view = new PostView();
    model = new Model(posts);
    view.displayPosts(posts);
}

class SelectController {
    static _tagSelect = document.getElementById('tagSelect');
    static _view = new HeaderView();

    static onTagsInput(value) {
        let tags = [];
        posts.forEach(post => post.hashTags.forEach(tag => tags.push(tag)));

        const uniqueTags = [...new Set(tags)];

        this._view.displayTagSelect(uniqueTags.filter(tag => tag.includes(value)));
    }
}

function addPost(post) {
    if (model.addPost(post)) {
        view.displayPost(post);
    }
}

function editPost(id, post) {
    if (model.editPost(id, post)) {
        view.refactorPost(id, model.getPost(id));
    }
}

function removePost(id) {
    if (model.removePost(id)) {
        view.removePost(id);
    }
}

function getPosts(skip, top, filterConfig) {
    let posts = model.getPage(skip, top, filterConfig);
    view.clearList();
    view.displayPosts(posts);
}