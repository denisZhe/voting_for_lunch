## Votes
**Geting all, by id, by user or lunch id is allowed only for admin. Creation or update is allowed for any authorized user.**

### GET /voting/rest/votes 
Returns a list of all votes ordered by date of voting, newer ones at the top.

#### Parameters: No parameters

#### Body: No body

#### Responses:
**Code: 200** (OK)

Example Value:
```json
[
  {
    "id": 100015,
    "votingDate": "2017-07-12",
    "userId": 100001,
    "lunchId": 100004
  },
  ...
]
```
```
curl -s http://localhost:8080/voting/rest/votes --user admin@gmail.com:adminpass
```

### GET /voting/rest/votes/{id}
Returns a single vote by id

#### Parameters: id - integer 

#### Body: No body

#### Responses:
**Code: 200** (OK)

Example Value:
```json
{
  "id": 100014,
  "votingDate": "2017-08-16",
  "userId": 100002,
  "lunchId": 100005
}
```
```
curl -s http://localhost:8080/voting/rest/votes/100014 --user admin@gmail.com:adminpass
```

**Code: 422** (Unprocessable Entity)

Returns if vote with specified id not found

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/votes/123",
  "cause": "NotFoundException",
  "details": [
    "Vote with such id doesn't exist"
  ]
}
```
```
curl -s http://localhost:8080/voting/rest/lunches/123 --user admin@gmail.com:adminpass
```

### GET /voting/rest/votes/by-user
Returns a list of votes by user id

#### Parameters: user - id of the user, integer

#### Body: No body

#### Responses:
**Code: 200** (OK)

Example Value:
```json
[
  {
    "id": 100013,
    "votingDate": "2017-06-20",
    "userId": 100002,
    "lunchId": 100004
  },
  ...
]
```
```
curl -s http://localhost:8080/voting/rest/votes/by-user?user=100002 --user admin@gmail.com:adminpass
```

**Code: 500** (Internal Server Error)

Returns if user id is null or specified with mistakes and could not be parsed

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/votes/by-user",
  "cause": "NumberFormatException",
  "details": [
    "For input string: \"abcdef\""
  ]
}
```
```
curl -s http://localhost:8080/voting/rest/votes/by-user?user=abcdef --user admin@gmail.com:adminpass
```

## GET /voting/rest/votes/by-lunch
Returns a list of votes by lunch id

#### Parameters: lunch - id of the lunch, integer

#### Body: No body

#### Responses:
**Code: 200** (OK)

Example Value:
```json
[
  {
    "id": 100013,
    "votingDate": "2017-06-20",
    "userId": 100002,
    "lunchId": 100004
  }
]
```
```
curl -s http://localhost:8080/voting/rest/votes/by-lunch?lunch=100004 --user admin@gmail.com:adminpass
```

**Code: 500** (Internal Server Error) - like when GET /voting/rest/votes/by-user

## POST /voting/rest/votes
Creates a new vote and returns it.  The date of creation of vote is set equal to the current date.

#### Parameters: No parameters

#### Body: vote object that needs to be created

Example Value:
```json
{
  "userId": 100001,
  "lunchId": 100004
}
```

#### Responses:
**Code: 201** (Created)

Example Value:
```json
{
  "id": 100015,
  "votingDate": "2017-08-16",
  "userId": 100001,
  "lunchId": 100004
}
```
```
for linux:
curl -s -X POST -d '{"userId": 100001, "lunchId": 100004}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/votes --user user@gmail.com:password
```
```
for windows:
curl -s -X POST -d "{\"userId\": 100001, \"lunchId\": 100004}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/votes --user user@gmail.com:password
```

**Code: 422** (Unprocessable Entity)

Returns if user id or lunch id is empty or in body (json) specified "votingDate" and it not equals current date 

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/votes",
  "cause": "ChangeUnacceptableException",
  "details": [
    "Creating or updating of vote is only allowed for today"
  ]
}
```
```
for linux:
curl -s -X POST -d '{"votingDate": "2017-07-16", "userId": 100001, "lunchId": 100004}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/votes --user user@gmail.com:password
```
```
for windows:
curl -s -X POST -d "{\"votingDate\": \"2017-07-16\", \"userId\": 100001, \"lunchId\": 100004}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/votes --user user@gmail.com:password
```

**Code: 409** (Conflict)

Returns trying to create a second vote from one user in one day

Example Value:
```json
{
  "url": "/voting/rest/votes",
  "cause": "HsqlException",
  "details": [
    "Only one vote per day for the user is possible"
  ]
}
```
```
for linux:
curl -s -X POST -d '{"userId": 100002, "lunchId": 100004}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/votes --user user2@gmail.com:password2
```
```
for windows:
curl -s -X POST -d "{\"userId\": 100002, \"lunchId\": 100004}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/votes --user user2@gmail.com:password2
```

**Code: 500** (Internal Server Error)

Returns if creating vote:
- specified with id (not new)
- user id or lunch id specified with mistakes and could not be parsed
- specified user id that does not match the id of the authorized user

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/votes",
  "cause": "IllegalArgumentException",
  "details": [
    "The specified user id=100002 does not match yours"
  ]
}
```
```
for linux:
curl -s -X POST -d '{"userId": 100002, "lunchId": 100004}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/votes --user user@gmail.com:password
```
```
for windows:
curl -s -X POST -d "{\"userId\": 100002, \"lunchId\": 100004}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/votes --user user@gmail.com:password
```

**Code: 405** (Method Not Allowed)

Returns if id specified in url

```
curl -s -X POST http://localhost:8080/voting/rest/votes/123 --user admin@gmail.com:adminpass
```

## PUT /voting/rest/votes/{id}
Updates the vote and returns it if current time is before deadline (11:00)

#### Parameters: id - integer

#### Body: vote object that needs to be updated

Example Value:
```json
{
  "id": 100015,
  "userId": 100001,
  "lunchId": 100005
}
```

#### Responses:
**Code: 200** (OK)

Example Value:
```json
{
  "id": 100015,
  "votingDate": "2017-08-17",
  "userId": 100001,
  "lunchId": 100005
}
```
```
for linux:
curl -s -X PUT -d '{"id": 100015, "userId": 100001, "lunchId": 100005}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/votes/100015 --user user@gmail.com:password
```
```
for windows:
curl -s -X PUT -d "{\"id\": 100015, \"userId\": 100001, \"lunchId\": 100005}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/votes/100015 --user user@gmail.com:password
```

**Code: 422** (Unprocessable Entity)

Returns if:
- user id or lunch id is empty
- in body (json) specified "votingDate" and it not equals current date
- in body (json) specified nonexistent lunch id
- it is after deadline (11:00)


Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/votes/100015",
  "cause": "ChangeUnacceptableException",
  "details": [
    "Too late for change vote"
  ]
}
```

To test it use curl for update vote when it is after 11:00

**Code: 409** (Conflict) - like when POST

**Code: 500** (Internal Server Error)

Returns if:
- id of updating vote specified in body (json) not match with id specified in url
- specified in body (json) user id not match the id of the authorized user

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/votes/100015",
  "cause": "IllegalArgumentException",
  "details": [
    "The specified user id=100002 does not match yours"
  ]
}
```
```
for linux:
curl -s -X PUT -d '{"id": 100015, "userId": 100002, "lunchId": 100005}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/votes/100015 --user user@gmail.com:password
```
```
for windows:
curl -s -X PUT -d "{\"id\": 100015, \"userId\": 100002, \"lunchId\": 100005}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/votes/100015 --user user@gmail.com:password
```

### General responses:
- Code: 401 Unauthorized
- Code: 403 Forbidden - returns if authorized user is not admin
