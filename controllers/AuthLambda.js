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



export const handler = async(event) => {
    let response = null;
    if (event.username === undefined || event === null || event.length === 0 || event === ''){
        response = {
            statusCode: 404,
            success: 'false', 
            message: JSON.stringify('Must provide the credentials for loggin.'),
        };
        return response;
    }

    let user = users.filter(item => item.user === event.username && item.password === event.password);
    if (user.length === 0){
        response = {
            statusCode: 404,
            success: 'false', 
            message: JSON.stringify('The user/password does not match with the right credentials.'),
        };
        return response;
    }

    response = {
            user: user[0].user,
            password: "******",
            name: user[0].name,
            lastname: user[0].lastname,
            emailname: user[0].emailname
    };
    return response;
};
