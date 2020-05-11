let posts = [
    {
        id: 1,
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
        id: 2,
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
        id: 3,
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
        id: 4,
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
        id: 5,
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
        id: 6,
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
        id: 7,
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
        id: 8,
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
        id: 9,
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
        id: 10,
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
];

if(localStorage.length === 0) {
    posts.forEach(post => localStorage.setItem(post.id, JSON.stringify(post)));
}