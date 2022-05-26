use auth
db.createUser(
    {
        user: 'alim',
        pwd: 'pass',
        roles: [ { role: 'root', db: 'auth' } ]
    }
);
