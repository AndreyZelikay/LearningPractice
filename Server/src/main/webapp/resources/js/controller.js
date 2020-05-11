class Controller {

    static pagination = 10;
    static postsOnScreen = 0;
    static filters = {};

    static containerClickEvent(event) {
        switch (event.target.className) {
            case 'button-refactor':
                Controller.refactorPostClick(event);
                break;
            case 'button-delete':
                Controller.deletePostClick(event);
                break;
            case "icon":
                Controller.likePostClick(event);
                break;
            case 'submit-button':
                if (event.target.parentElement.id !== 'twitAdd') {
                    Controller.submitRefactorClick(event);
                } else {
                    Controller.addPostClick(event);
                }

                break;
            case 'see-more':
                Controller.seeMoreClick(event);
                break;
        }
    }

    static addPostClick(event) {
        event.preventDefault();

        const post = {};
        post.description = event.target.parentElement.elements.description.value;
        post.hashTags = event.target.parentElement.elements.hashTags.value;

        if (model.addPost(post)) {
            if (this.postsOnScreen % 10 !== 0) {
                view.displayPost(post);
                this.postsOnScreen++;
            }
            event.target.parentElement.reset();
        }
    }

    static refactorPostClick(event) {
        const post = model.getPost(Number.parseInt(event.target.parentElement.parentElement.parentElement.id));
        view.displayRefactoring(post);
    }

    static submitRefactorClick(event) {
        event.preventDefault();
        const refactorElement = event.target.parentElement;
        const id = Number.parseInt(event.target.parentElement.id);

        let postForm = {};
        postForm.description = event.target.parentElement.elements.description.value;
        postForm.hashTags = event.target.parentElement.elements.hashTags.value;

        const currentPost = model.getPost(id);

        if (model.editPost(id, postForm)) {
            const refactoredPost = model.getPost(id);
            view.replaceRefactorWithPost(refactorElement, refactoredPost);
            return;
        }

        view.replaceRefactorWithPost(refactorElement, currentPost);
    }

    static deletePostClick(event) {
        const id = Number.parseInt(event.target.parentElement.parentElement.parentElement.id);
        if (model.removePost(id)) {
            view.removePost(id);
        }
    }

    static likePostClick(event) {
        if (!localStorage.getItem('user')) {
            return;
        }
        const id = Number.parseInt(event.target.parentElement.parentElement.parentElement.parentElement.id);
        const post = model.getPost(id);
        model.changeLikes(post);
        view.refreshPostLikes(post);
    }

    static seeMoreClick(event) {
        const posts = model.getPage(Controller.postsOnScreen, Controller.pagination, Controller.filters);
        Controller.postsOnScreen += posts.length;
        view.displayPosts(posts);
    }

    static displayFiltrationClick(event) {
        event.preventDefault();
        view.displayFiltration();
    }

    static onFiltersChange(event) {
        view.clearList();
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

        Controller.filters = filters;

        const posts = model.getPage(0, Controller.pagination, filters);
        Controller.postsOnScreen = posts.length;
        view.displayPosts(posts);
    }

    static loginEvent(event) {
        if (localStorage.getItem('user')) {
            Controller.logOutClick();
        } else {
            Controller.loginClick();
        }
    }

    static loginClick(event) {
        const user = prompt('Input your login');

        if (user.length !== 0) {
            localStorage.setItem('user', user);
            view.displayCurrentUser(localStorage.getItem('user'));
            view.displayPostCreation();
            Controller.refreshPostList();
        } else {
            alert('incorrect login');
        }
    }

    static refreshPostList() {
        view.clearList();
        const posts = model.getPage(0, 10);
        Controller.filters = null;
        view.clearFiltrationForm();
        Controller.postsOnScreen = posts.length;
        console.log(posts);
        view.displayPosts(posts);
    }

    static logOutClick(event) {
        view.displayLogIn();
        view.hidePostCreation();
        localStorage.removeItem('user');
        Controller.refreshPostList();
    }
}

let model;
let view;

window.onload = () => {
    model = new Model();
    view = new View();

    localStorage.setItem('userPhoto', 'https://m2bob-forum.net/wcf/images/avatars/3e/2720-3e546be0b0701e0cb670fa2f4fcb053d4f7e1ba5.jpg');

    document.getElementById('container').addEventListener('click', Controller.containerClickEvent);
    document.getElementById('menu-button').addEventListener('click', Controller.displayFiltrationClick);
    document.getElementById('filtration').addEventListener('change', Controller.onFiltersChange);
    document.getElementById('filtration').hashTags.addEventListener('input', Controller.onFiltersChange);
    document.getElementById('login-button').addEventListener('click', Controller.loginEvent);

    if (!localStorage.getItem('user')) {
        view.displayLogIn();
    } else {
        view.displayCurrentUser(localStorage.getItem('user'));
        view.displayPostCreation();
    }

    const posts = model.getPage(0, 10);
    Controller.postsOnScreen = posts.length;
    view.displayPosts(posts);
};