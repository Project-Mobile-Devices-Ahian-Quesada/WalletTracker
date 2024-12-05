'use strict';

const users =  [
    {
        user: "estudiante",
        password: "123456",
        name: "Estudiante",
        lastname: "Curso Calidad y Pruebas de Software",
        emailname: "Curso, Calidad"
    }, 
    {
        user: "jose1234",
        password: "123456",
        name: "Jose",
        lastname: "V",
        emailname: "V, Jose"
    }, 
    {
        user: "carlos1234",
        password: "123456",
        name: "Carlos",
        lastname: "Mora",
        emailname: "Mora, Carlos"
    }
];

exports.authUser = function(req, res) {
    if (req.body.username === undefined || req.body === null || req.body.length === 0 || req.body === ''){
        res.status(404).send({ success: 'false', message: 'Must provide the credentials for loggin.' });
        return;
    }

    let user = users.filter(item => item.user === req.body.username && item.password === req.body.password);
    if (user.length === 0){
        res.status(404).send({ success: 'false', message: 'The user/password does not match with the right credentials.' });
        return;
    }

    let loggedUser = {
        user: user[0].user,
        password: "******",
        name: user[0].name,
        lastname: user[0].lastname,
        emailname: user[0].emailname
    };
    res.status(200).send(loggedUser);
};