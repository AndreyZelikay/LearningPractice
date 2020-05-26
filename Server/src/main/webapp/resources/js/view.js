class View {
    _currentUser;
    _postTemplate = document.getElementById('twit-template');
    _postContainer = document.getElementById('container');
    _postCreation = document.getElementById('twitAdd');
    _header = document.getElementById("header");

    _fillPostData(postTemplate, data) {
        if (this._currentUser !== data.author.name) {
            postTemplate.querySelector('[class = "twit-buttons"]').setAttribute('style', 'display: none')
        }

        postTemplate.firstElementChild.setAttribute('id', data.id);

        if (data?.photoLink != null) {
            postTemplate.querySelector('[data-target = "photoLink"]').setAttribute('src', data.photoLink);
        } else {
            postTemplate.querySelector('[data-target = "photoLink"]').setAttribute('style', 'display: none');
        }

        postTemplate.querySelector('[data-target = "user-icon"]').setAttribute('src', localStorage.getItem('userPhoto'));
        postTemplate.querySelector('[data-target = "description"]').textContent = data.description;
        postTemplate.querySelector('[data-target = "createdAt"]').textContent = data.createdAt.toLocaleDateString('en-US');
        postTemplate.querySelector('[data-target = "author"]').textContent = data.author.name;
        postTemplate.querySelector('[data-target = "hashTags"]').textContent = String(data.hashTags?.map(item => '#' + item.body) || []);
        postTemplate.querySelector('[data-target = "likes"]').textContent = String((data.likes || []).length);
    }

    displayPost(post) {
        let postNode = document.importNode(this._postTemplate.content, true);

        this._fillPostData(postNode, post);

        this._postContainer.insertBefore(postNode, this._postContainer.lastElementChild);
    }

    displayRefactoring(post) {
        const postRefactoring = document.importNode(this._postCreation, true);

        postRefactoring.hashTags.value = String(post.hashTags.map(item => '#' + item.body).join(''));
        postRefactoring.description.value = post.description;
        postRefactoring.id = post.id;

        document.getElementById(post.id).replaceWith(postRefactoring);
    }

    replaceRefactorWithPost(refactor, post) {
        let postNode = document.importNode(this._postTemplate.content, true);

        this._fillPostData(postNode, post);

        refactor.replaceWith(postNode);
    }

    removePost(id) {
        document.getElementById(id)?.remove();
    }

    clearList() {
        let first = this._postContainer.children[1];

        while (first && first !== this._postContainer.lastElementChild) {
            first.remove();
            first = this._postContainer.children[1];
        }
    }

    refreshPostLikes(postId, isIncrease) {
        let postLikes = document.getElementById(postId).querySelector('[data-target = "likes"]');
        const numberLikes = Number.parseInt(postLikes.textContent);
        postLikes.textContent = (isIncrease) ? String(numberLikes + 1) : String(numberLikes - 1);
    }

    displayPosts(posts) {
        posts.forEach(post => this.displayPost(post));
    }

    displayCurrentUser(user) {
        this._currentUser = user;
        this._header.querySelector('[class = "my-profile"]').textContent = this._currentUser;
        this._header.querySelector('[class = "my-profile"]')
            .setAttribute('style', 'display: block');
        this._header.querySelector('[class = "button log-out"]').firstElementChild.textContent = 'Log out';
    }

    displayFiltration() {
        const filtration = document.getElementById("filters");

        if (filtration.style.display !== 'none') {
            filtration.setAttribute('style', 'display: none');
        } else {
            filtration.setAttribute('style', 'display: inline-block');
        }
    }

    displayLogIn() {
        this._currentUser = '';
        this._header.querySelector('[class = "my-profile"]')
            .setAttribute('style', 'display: none');
        this._header.querySelector('[class = "button log-out"]').firstElementChild.textContent = 'Log in';
    }

    hidePostCreation() {
        this._postCreation.setAttribute('style', 'display: none');
    }

    displayPostCreation() {
        this._postCreation.setAttribute('style', 'display: inline-block');
    }

    clearFiltrationForm() {
        document.forms.filtration.reset();
    }
}