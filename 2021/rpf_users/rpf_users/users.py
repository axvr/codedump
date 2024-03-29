from . import app, crypto
from .db import open_db
from .vresult import VResult

import re
from datetime import datetime
from flask import jsonify, request


# -------------------------------------------------------------
# Validation helpers.


def is_username_available(username):
    """Returns true if 'username' is available for use."""
    with open_db() as db:
        return None is db.exec(
            'SELECT id from users WHERE username = :username LIMIT 1',
            {'username': username}).fetchone()


username_regex = re.compile(r'^[a-zA-Z0-9_-]{2,10}$')


def is_valid_username(username):
    """Checks if a given username is valid and returns a VResult object."""
    reason = None
    if not isinstance(username, str):
        reason = 'Username must be a string.'
    elif 2 > len(username) or len(username) > 10:
        reason = 'Username length must be between 2 and 10 characters.'
    elif not username_regex.match(username):
        reason = 'Username may only contain alphanumeric characters, hyphens and underscores.'
    elif not is_username_available(username):
        reason = 'Username already in use.'
    return VResult(reason)


email_addr_regex = re.compile(r'^.*?@.*$')


def is_valid_email_address(email_addr):
    """
    Simple email address validation.  Checks if a given email address is valid
    and returns a VResult object.

    To validate an email address properly you need to send a confirmation email
    to the address containing a link for the user to click.
    """
    if not email_addr_regex.match(email_addr):
        return VResult('Email address is missing an \'@\' symbol.')
    return VResult()


def is_valid_user_model(user):
    """Checks if a given user model is valid and returns a VResult object."""

    if user is None:
        return VResult('No information given.')

    if 'username' not in user:
        return VResult('No username given.')
    username = is_valid_username(user['username'])
    if not username.is_valid:
        return username

    if 'password' not in user or len(user['password'] or '') == 0:
        return VResult('No password given.')

    if 'email_address' not in user:
        return VResult('No email address given.')
    return is_valid_email_address(user['email_address'])



# -------------------------------------------------------------
# Routes.


@app.route("/")
def index():
    return '<p>Hello, rpf_users!</p>'


def create_user(user):
    """
    Create a new user.

    Expects a user dictionary with the following info:
      - username
      - email_address
      - password

    If successful, returns a dictionary containing the ID of the user.

    If unsuccessful, returns a dictionary containing the failure reason.
    """
    validation_res = is_valid_user_model(user)
    if validation_res.is_valid:
        with open_db() as db:
            db.exec('''
                INSERT INTO users
                (username, email_address, password_hash, created_at)
                VALUES
                (:username, :email_address, :password_hash, :created_at)
                ''',
                {
                    'username':      user['username'],
                    'email_address': user['email_address'],
                    'password_hash': crypto.hash_password(user['password']),
                    'created_at':    datetime.utcnow().isoformat()
                })
            return {'id': db.cur.lastrowid}
    else:
        return {'error': validation_res.reason}


@app.route("/users", methods=["POST"])
def create_user_endpoint():
    resp = create_user(request.json)
    if "error" in resp:
        code = 400
    else:
        code = 200
    return jsonify(resp), code


def get_users():
    """Returns a list of users."""
    with open_db() as db:
        users = db.exec('SELECT id, username FROM users').fetchall()
        return {'users': list(map(dict, users))}


@app.route("/users", methods=["GET"])
def get_users_endpoint():
    users = get_users()
    return jsonify(users)


def get_user(id):
    """Returns details on a specific user."""
    with open_db() as db:
        user = db.exec('''
            SELECT id, username, email_address, is_active, created_at
            FROM users
            WHERE id = :id
            LIMIT 1
            ''',
            {'id': id}).fetchone()
        if user:
            return dict(user)


@app.route("/users/<int:id>", methods=["GET"])
def get_user_endpoint(id):
    user = get_user(id)
    if user:
        return jsonify(user)
    else:
        return jsonify({'error': 'User not found.'}), 404


def delete_user(id):
    """Deletes user with specified id."""
    with open_db() as db:
        db.exec('DELETE FROM users WHERE id = :id', {'id': id})


@app.route("/users/<int:id>", methods=["DELETE"])
def delete_user_endpoint(id):
    if 'X-API-KEY' in request.headers:
        if request.headers['X-API-KEY'] == app.config["SECRET_KEY"]:
            delete_user(id)
            return {}
    return jsonify({'error': 'Invalid X-API-KEY header value.'}), 401


def set_active(id, is_active):
    """Set if a user is active or inactive."""
    with open_db() as db:
        if is_active:
            is_active = 1
        else:
            is_active = 0
        db.exec('UPDATE users SET is_active = :active WHERE id = :id',
                {'id': id, 'active': is_active})


@app.route("/users/<int:id>/active", methods=["POST"])
def set_active_endpoint(id):
    set_active(id, is_active=True)
    return {}


@app.route("/users/<int:id>/inactive", methods=["POST"])
def set_inactive_endpoint(id):
    set_active(id, is_active=False)
    return {}
