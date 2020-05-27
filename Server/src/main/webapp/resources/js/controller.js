class Controller {
    _model;
    _view;
    _pagination = 10;
    _postsOnScreen = 0;
    _filters = {};

    constructor(model, view) {
        this._model = model;
        this._view = view;
        this._filters = {
            skip: 0,
            top: this._pagination,
        };
    }

    setPostsOnScreen(postsOnScreen) {
        this._postsOnScreen = postsOnScreen;
    }

    async containerClickEvent(event) {
        switch (event.target.className) {
            case 'button-refactor':
                await this.refactorPostClick(event);
                break;
            case 'button-delete':
                await this.deletePostClick(event);
                break;
            case "icon":
                await this.likePostClick(event);
                break;
            case 'submit-button':
                if (event.target.parentElement.id !== 'twitAdd') {
                    await this.submitRefactorClick(event);
                } else {
                    await this.addPostClick(event);
                }

                break;
            case 'see-more':
                await this.seeMoreClick(event);
                break;
        }
    }

    async addPostClick(event) {
        event.preventDefault();

        let post = {
            description: event.target.parentElement.elements.description.value,
            hashTags: event.target.parentElement.elements.hashTags.value
        };

        if (!this._model.validateForm(post)) {
            return;
        }

        try {
            post = await this._model.addPost(post);
            post.createdAt = new Date(post.createdAt);

            if (this._postsOnScreen % 10 !== 0) {
                this._view.displayPost(post);
                this._postsOnScreen++;
            }

            event.target.parentElement.reset();
        } catch (e) {
            alert('unable to create twit: ' + e);
        }
    }

    async refactorPostClick(event) {
        const post = await this._model.getPost(Number.parseInt(event.target.parentElement.parentElement.parentElement.id));
        localStorage.setItem('refactoringPost', JSON.stringify(post));
        this._view.displayRefactoring(post);
    }

    async submitRefactorClick(event) {
        event.preventDefault();
        const refactorElement = event.target.parentElement;
        const id = Number.parseInt(refactorElement.id);

        const updateForm = {
            description: refactorElement.elements.description.value,
            hashTags: refactorElement.elements.hashTags.value,
        };

        if (!this._model.validateForm(updateForm)) {
            return;
        }

        const currentPost = JSON.parse(localStorage.getItem('refactoringPost'));
        currentPost.createdAt = new Date(currentPost.createdAt);

        try {
            const refactoredPost = await this._model.editPost(id, updateForm);
            refactoredPost.createdAt = new Date(refactoredPost.createdAt);
            this._view.replaceRefactorWithPost(refactorElement, refactoredPost);
        } catch (e) {
            this._view.replaceRefactorWithPost(refactorElement, currentPost);
            alert(e);
        }
    }

    async deletePostClick(event) {
        const id = Number.parseInt(event.target.parentElement.parentElement.parentElement.id);
        try {
            const response = await this._model.removePost(id);

            if (response) {
                this._view.removePost(id);
            }
        } catch (e) {
            alert(e);
        }
    }

    async likePostClick(event) {
        if (!localStorage.getItem('userName')) {
            return;
        }

        const id = Number.parseInt(event.target.parentElement.parentElement.parentElement.parentElement.id);

        try {
            const response = await this._model.changeLikes(id);

            if (response !== 'inc' && response !== 'dec') {
                return;
            }

            this._view.refreshPostLikes(id, response === 'inc');
        } catch (e) {
            alert(e);
        }
    }

    async seeMoreClick() {
        try {
            this._filters.skip = this._postsOnScreen;
            this._filters.top = this._pagination;
            const posts = await this._model.getPage(this._filters);
            this._postsOnScreen += posts.length;
            this._view.displayPosts(posts);
        } catch (e) {
            alert(e);
        }
    }

    displayFiltrationClick(event) {
        event.preventDefault();
        this._view.displayFiltration();
    }

    async onFiltersChange(event) {
        let filter = event.target;
        while (filter.elements == null) {
            filter = filter.parentElement;
        }

        const formElements = filter.elements;
        const filters = {};

        if (formElements.author.value.length !== 0) {
            filters.author = formElements.author.value;
        }

        if (formElements.dateFrom.value.length !== 0) {
            filters.fromDate = formElements.dateFrom.value;
        }

        if (formElements.dateTo.value.length !== 0) {
            filters.untilDate = formElements.dateTo.value;
        }

        filters.hashTags = formElements.hashTags.value.split(' ').filter(tag => tag !== '');
        filters.skip = 0;
        filters.top = this._pagination;

        this._filters = filters;

        try {
            const posts = await this._model.getPage(filters);
            this._view.clearList();
            this._view.displayPosts(posts);
            this._postsOnScreen = posts.length;
        } catch (e) {
            alert(e);
        }
    }

    async loginEvent() {
        if (localStorage.getItem('userName')) {
            await this.logOutClick();
        } else {
            this.loginClick();
        }
    }

    loginClick() {
        this._view.displayLoginWindow();
    }

    async loginSubmit(event) {
        event.preventDefault();

        let user = {
            name: event.target.elements.name.value,
            password: event.target.elements.password.value
        };

        if (user.name.length === 0 || user.password.length === 0) {
            alert('incorrect login');
            return;
        }

        try {
            user = await this._model.loginUser(user);

            if (user == null) {
                alert('invalid name or password');
                return;
            }

            localStorage.setItem('userName', user.name);
            event.target.reset();
            this._view.hideLoginWindow();
            this._view.displayCurrentUser(user.name);
            this._view.displayPostCreation();
            await this.refreshPostList();
        } catch (e) {
            alert(e);
        }
    }

    async refreshPostList() {
        this._view.clearList();
        const posts = await this._model.getPage(this._filters);
        this._view.clearFiltrationForm();
        this._postsOnScreen = posts.length;
        this._view.displayPosts(posts);
    }

    async logOutClick() {
        this._view.displayLogInButton();
        this._view.hidePostCreation();
        localStorage.removeItem('userName');
        await this._model.logOutUser();
        await this.refreshPostList();
    }
}

window.onload = async () => {
    const view = new View();
    const model = new Model();
    const controller = new Controller(model, view);

    localStorage.setItem('userPhoto', 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg');

    document.getElementById('container').addEventListener('click', (event) => controller.containerClickEvent(event));
    document.getElementById('menu-button').addEventListener('click', (event) => controller.displayFiltrationClick(event));
    document.getElementById('filtration').addEventListener('change', (event) => controller.onFiltersChange(event));
    document.getElementById('filtration').hashTags.addEventListener('input', (event) => controller.onFiltersChange(event));
    document.getElementById('login-button').addEventListener('click', (event) => controller.loginEvent(event));
    document.getElementById('login').addEventListener('submit', (event) => controller.loginSubmit(event));

    const constantMock = window.fetch;

    window.fetch = function () {
        return new Promise((resolve, reject) => {
            constantMock.apply(this, arguments)
                .then((response) => {
                    if (response.status === 403) {
                        alert('your session expired');
                        controller.logOutClick();
                    }

                    resolve(response);
                })
                .catch((error) => {
                    reject(error);
                })
        });
    }

    if (!localStorage.getItem('userName')) {
        view.displayLogInButton();
    } else {
        view.displayCurrentUser(localStorage.getItem('userName'));
        view.displayPostCreation();
    }

    try {
        const posts = await model.getPage(controller._filters);
        controller.setPostsOnScreen(posts.length);
        view.displayPosts(posts);
    } catch (e) {
        alert(e);
    }
};