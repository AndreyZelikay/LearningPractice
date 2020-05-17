class Controller {
    _model;
    _view;
    _pagination = 10;
    _postsOnScreen = 0;
    _filters = {};

    constructor(model, view) {
        this._model = model;
        this._view = view;
    }

    setPostsOnScreen(postsOnScreen) {
        this._postsOnScreen = postsOnScreen;
    }

    containerClickEvent(event) {
        switch (event.target.className) {
            case 'button-refactor':
                this.refactorPostClick(event);
                break;
            case 'button-delete':
                this.deletePostClick(event);
                break;
            case "icon":
                this.likePostClick(event);
                break;
            case 'submit-button':
                if (event.target.parentElement.id !== 'twitAdd') {
                    this.submitRefactorClick(event);
                } else {
                    this.addPostClick(event);
                }

                break;
            case 'see-more':
                this.seeMoreClick(event);
                break;
        }
    }

    addPostClick(event) {
        event.preventDefault();

        const post = {};
        post.description = event.target.parentElement.elements.description.value;
        post.hashTags = event.target.parentElement.elements.hashTags.value;

        if (this._model.addPost(post)) {
            if (this._postsOnScreen % 10 !== 0) {
                this._view.displayPost(post);
                this._postsOnScreen++;
            }
            event.target.parentElement.reset();
        }
    }

    refactorPostClick(event) {
        const post = this._model.getPost(Number.parseInt(event.target.parentElement.parentElement.parentElement.id));
        this._view.displayRefactoring(post);
    }

    submitRefactorClick(event) {
        event.preventDefault();
        const refactorElement = event.target.parentElement;
        const id = Number.parseInt(event.target.parentElement.id);

        let postForm = {};
        postForm.description = event.target.parentElement.elements.description.value;
        postForm.hashTags = event.target.parentElement.elements.hashTags.value;

        const currentPost = this._model.getPost(id);

        if (this._model.editPost(id, postForm)) {
            const refactoredPost = this._model.getPost(id);
            this._view.replaceRefactorWithPost(refactorElement, refactoredPost);
            return;
        }

        this._view.replaceRefactorWithPost(refactorElement, currentPost);
    }

    deletePostClick(event) {
        const id = Number.parseInt(event.target.parentElement.parentElement.parentElement.id);
        if (this._model.removePost(id)) {
            this._view.removePost(id);
        }
    }

    likePostClick(event) {
        if (!localStorage.getItem('user')) {
            return;
        }
        const id = Number.parseInt(event.target.parentElement.parentElement.parentElement.parentElement.id);
        const post = this._model.getPost(id);
        this._model.changeLikes(post);
        this._view.refreshPostLikes(post);
    }

    seeMoreClick(event) {
        const posts = this._model.getPage(this._postsOnScreen, this._pagination, this._filters);
        this._postsOnScreen += posts.length;
        this._view.displayPosts(posts);
    }

    displayFiltrationClick(event) {
        event.preventDefault();
        this._view.displayFiltration();
    }

    onFiltersChange(event) {
        this._view.clearList();
        let filter = event.target;
        while (filter.elements == null) {
            filter = filter.parentElement;
        }

        const formElements = filter.elements;
        const filters = {};
        filters.author = formElements.author.value;
        filters.dateFrom = new Date(formElements.dateFrom.value);
        filters.dateTo = new Date(formElements.dateTo.value);
        filters.hashTags = formElements.hashTags.value.split(' ').filter(tag => tag !== '');

        this._filters = filters;

        const posts = this._model.getPage(0, this._pagination, filters);
        this._postsOnScreen = posts.length;
        this._view.displayPosts(posts);
    }

    loginEvent(event) {
        if (localStorage.getItem('user')) {
            this.logOutClick();
        } else {
            this.loginClick();
        }
    }

    loginClick(event) {
        const user = prompt('Input your login');

        if (user.length !== 0) {
            localStorage.setItem('user', user);
            this._view.displayCurrentUser(localStorage.getItem('user'));
            this._view.displayPostCreation();
            this.refreshPostList();
        } else {
            alert('incorrect login');
        }
    }

    refreshPostList() {
        this._view.clearList();
        const posts = this._model.getPage(0, 10);
        this._filters = null;
        this._view.clearFiltrationForm();
        this._postsOnScreen = posts.length;
        this._view.displayPosts(posts);
    }

    logOutClick(event) {
        this._view.displayLogIn();
        this._view.hidePostCreation();
        localStorage.removeItem('user');
        this.refreshPostList();
    }
}

window.onload = () => {
    const view = new View();
    const model = new Model();
    const controller = new Controller(model, view);

    localStorage.setItem('userPhoto', 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg');

    document.getElementById('container').addEventListener('click', (event) => controller.containerClickEvent(event));
    document.getElementById('menu-button').addEventListener('click', (event) => controller.displayFiltrationClick(event));
    document.getElementById('filtration').addEventListener('change', (event) => controller.onFiltersChange(event));
    document.getElementById('filtration').hashTags.addEventListener('input', (event) => controller.onFiltersChange(event));
    document.getElementById('login-button').addEventListener('click', (event) => controller.loginEvent(event));

    if (!localStorage.getItem('user')) {
        view.displayLogIn();
    } else {
        view.displayCurrentUser(localStorage.getItem('user'));
        view.displayPostCreation();
    }

    const posts = model.getPage(0, 10);
    controller.setPostsOnScreen(posts.length);
    view.displayPosts(posts);
};