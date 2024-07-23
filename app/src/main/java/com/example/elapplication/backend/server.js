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

app.get('/factures/unpaid', (req, res) => {
    const { phoneNumber, billNumber } = req.query;
    const status = 'unpaid';

    console.log('Received request:', { status, phoneNumber, billNumber });

    // Construct the query based on provided parameters
    let query = 'SELECT * FROM facture WHERE etat = ?';
    const params = [status];

    if (phoneNumber) {
        query += ' AND num_tel = ?';
        params.push(phoneNumber);
    } else if (billNumber) {
        query += ' AND num_facture = ?';
        params.push(billNumber);
    }

    console.log('Executing query:', query, 'with params:', params);

    // Execute the query (this example uses a generic db.query method)
    db.query(query, params, (error, results) => {
        if (error) {
            console.error('Database query error:', error);
            res.status(500).send('Internal Server Error');
        } else {
            console.log('Query Results:', results);
            res.send(results);
        }
    });
});

app.post('/update', (req, res) => {
    const { numFacture, datePayment } = req.query;
    const query = 'UPDATE facture SET etat = "paid", date_payment = ? WHERE num_facture = ?';
    const params = [datePayment, numFacture];

    db.query(query, params, (err, result) => {
        if (err) throw err;
        res.sendStatus(200);
    });
});

app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});
