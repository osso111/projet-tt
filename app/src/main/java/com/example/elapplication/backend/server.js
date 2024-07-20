//server.js
const express = require('express');
const mysql = require('mysql');
const bodyParser = require('body-parser');
const app = express();
const port = 3000;

const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'telecom'
});

db.connect(err => {
    if (err) {
        throw err;
    }
    console.log('MySQL Connected...');
});

app.use(bodyParser.json());


app.post('/login', (req, res) => {
    console.log("Login request received");
    const { phone_number, password } = req.body;
    console.log(`Phone Number: ${phone_number}, Password: ${password}`);
    const query = 'SELECT * FROM clients WHERE `phone number` = ? AND password = ?';
    db.query(query, [phone_number, password], (err, results) => {
        if (err) {
            console.error('Database query error:', err);
            return res.status(500).send(err);
        }

        console.log('Query Results:', results);
        if (results.length > 0) {
            res.json({ success: true });
        } else {
            res.json({ success: false });
        }
    });
});

app.post('/signup', (req, res) => {
    console.log("Signup request received");
    const { cin, username, phone_number, email, password } = req.body;
    console.log(`CIN: ${cin}, Username: ${username}, Phone Number: ${phone_number}, Email: ${email}, Password: ${password}`);
    const query = 'INSERT INTO clients (cin, username, `phone number`, email, password) VALUES (?, ?, ?, ?, ?)';
    db.query(query, [cin, username, phone_number, email, password], (err, results) => {
        if (err) {
            console.error('Database query error:', err);
            return res.status(500).send(err);
        }

        console.log('Insert Results:', results);
        res.json({ success: true });
    });
});

app.get('/facture', (req, res) => {
    const state = req.query.state;
    const phoneNumber = req.query.phoneNumber;
    const billNumber = req.query.billNumber;

    console.log(`Received request: state=${state}, phoneNumber=${phoneNumber}, billNumber=${billNumber}`);

    let query = 'SELECT * FROM facture WHERE etat = ?';
    let queryParams = [state];

    if (phoneNumber) {
        query += ' AND num_tel = ?';
        queryParams.push(phoneNumber);
        console.log(`Querying by phone number: ${phoneNumber}`);
    } else if (billNumber) {
        query += ' AND num_facture = ?';
        queryParams.push(billNumber);
        console.log(`Querying by bill number: ${billNumber}`);
    }

    console.log(`Executing query: ${query} with params: ${queryParams}`);

    db.query(query, queryParams, (err, results) => {
        if (err) {
            console.error(`Error executing query: ${err.message}`);
            res.status(500).send(err);
        } else {
        console.log('Query Results:', results);
            res.json(results);
        }
    });
});

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
