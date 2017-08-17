## Lunches
**Allowed for admin.**

### GET /voting/rest/lunches 
Returns a list of all lunches ordered by date of creating lunches, newer ones at the top.

####Parameters: No parameters

#### Body: No body

####Responses:
**Code: 200** (OK)

Example Value:
```json
[
  {
    "id": 100005,
    "created": "2017-08-03",
    "restaurantName": "test_restaurant_name"
  },
  ...
]
```
```
curl -s http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```

### GET /voting/rest/lunches/{id}
Returns a single lunch by id

####Parameters: id - integer 

#### Body: No body

####Responses:
**Code: 200** (OK)

Example Value:
```json
{
  "id": 100005,
  "created": "2017-08-03",
  "restaurantName": "test_restaurant_name"
}
```
```
curl -s http://localhost:8080/voting/rest/lunches/100005 --user admin@gmail.com:adminpass
```

**Code: 422** (Unprocessable Entity)

Returns if lunch with specified id not found

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/lunches/123",
  "cause": "NotFoundException",
  "details": [
      "The lunch with such id doesn't exist"
    ]
}
```
```
curl -s http://localhost:8080/voting/rest/lunches/123 --user admin@gmail.com:adminpass
```

### GET /voting/rest/lunches/by-date
Returns a list of lunches by date of creating

####Parameters: date - date of creating, formatted like yyyy-mm-dd

#### Body: No body

####Responses:
**Code: 200** (OK)

Example Value:
```json
[
  {
    "id": 100004,
    "created": "2017-06-20",
    "restaurantName": "test_restaurant_name"
  },
  ...
]
```
```
curl -s http://localhost:8080/voting/rest/lunches/by-date?date=2017-06-20 --user admin@gmail.com:adminpass
```

**Code: 500** (Internal Server Error)

Returns if date of creating is null or specified with mistakes and could not be parsed

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/lunches/by-date",
  "cause": "IllegalArgumentException",
  "details": [
    "Date must not be null"
   ]
}
```
```
curl -s http://localhost:8080/voting/rest/lunches/by-date?date=abcdef --user admin@gmail.com:adminpass
```

## GET /voting/rest/lunches/detailed-by-date
Returns a list of lunches with meals by date of creating

####Parameters: date - date of creating, formatted like yyyy-mm-dd

#### Body: No body

####Responses:
**Code: 200** (OK)

Example Value:
```json
[
  {
    "id": 100004,
    "created": "2017-06-20",
    "restaurantName": "test_restaurant_name",
    "meals": [
      {
        "id": 100008,
        "created": "2017-06-20",
        "dishName": "dish_name",
        "price": 10
      },
      ...
    ]
  },
  ...
]
```
```
curl -s http://localhost:8080/voting/rest/lunches/detailed-by-date?date=2017-06-20 --user admin@gmail.com:adminpass
```

**Code: 500** (Internal Server Error) - like when GET /voting/rest/lunches/by-date

## POST /voting/rest/lunches
Creates a new lunch with enclosed meal and returns it. The date of creation of lunch and enclosed meal is set equal to the current date.

####Parameters: No parameters

#### Body: lunch object that needs to be created

Example Value:
```json
{
  "restaurantName": "restaurant_test_2",
  "meals": [
    {
      "dishName": "dish_test_1",
      "price": 100
    },
    {
      "dishName": "dish_test_22",
      "price": 200
    }
  ]
}
```

####Responses:
**Code: 201** (Created)

Example Value:
```json
{
  "id": 100015,
  "created": "2017-08-10",
  "restaurantName": "restaurant_test_2",
  "meals": [
    {
      "id": 100016,
      "created": "2017-08-10",
      "dishName": "dish_test_1",
      "price": 100
    },
    {
      "id": 100017,
      "created": "2017-08-10",
      "dishName": "dish_test_22",
      "price": 200
    }
  ]
}
```
```
for linux:
curl -s -X POST -d '{"restaurantName": "restaurant_test_2", "meals": [{"dishName": "dish_test_1", "price": 100}, {"dishName": "dish_test_22", "price": 200}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```
```
for windows:
curl -s -X POST -d "{\"restaurantName\": \"restaurant_test_2\", \"meals\": [{\"dishName\": \"dish_test_1\", \"price\": 100}, {\"dishName\": \"dish_test_22\", \"price\": 200}]}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```

**Code: 422** (Unprocessable Entity)

Returns if restaurant name or dish name or price is empty 

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/lunches",
  "cause": "ValidationException",
  "details": [
      "meals[0].dishName must not be empty",
      "meals[0].price must be specified"
  ]
}
```
```
for linux:
curl -s -X POST -d '{"restaurantName": "restaurant_test_7", "meals": [{"dishName": "", "price": ""}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```
```
for windows:
curl -s -X POST -d "{\"restaurantName\": \"restaurant_test_7\", \"meals\": [{\"dishName\": \"\", \"price\": \"\"}]}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```

**Code: 409** (Conflict)

Returns if lunch specified with repeated dish name or if already exists lunch with such restaurant name and date of creation

Example Value:
```json
{
  "url": "/voting/rest/lunches",
  "cause": "HsqlException",
  "details": [
    "Dishes with the same name are not allowed in one lunch"
  ]
}
```
```
for linux:
curl -s -X POST -d '{"restaurantName": "restaurant_test_2", "meals": [{"dishName": "dish_test_1", "price": 100}, {"dishName": "dish_test_1", "price": 200}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```
```
for windows:
curl -s -X POST -d "{\"restaurantName\": \"restaurant_test_2\", \"meals\": [{\"dishName\": \"dish_test_1\", \"price\": 100}, {\"dishName\": \"dish_test_1\", \"price\": 200}]}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```

**Code: 500** (Internal Server Error)

Returns if creating lunch specified with id (not new)

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/lunches",
  "cause": "IllegalArgumentException",
  "details": [
    "Lunch{id=100004, created=2017-08-14, restaurantName='restaurant_test_8', meals=[Meal{id=null, created=2017-08-14, dishName='dish_test_1', price=100}]} must be new (id=null)"
  ]
}
```
```
for linux:
curl -s -X POST -d '{"id": 100004, "restaurantName": "restaurant_test_2", "meals": [{"dishName": "dish_test_1", "price": 100}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```
```
for windows:
curl -s -X POST -d "{\"id\": 100004, \"restaurantName\": \"restaurant_test_2\", \"meals\": [{\"dishName\": \"dish_test_1\", \"price\": 100}]}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```

## PUT /voting/rest/lunches/{id}
Updates the lunch and/or enclosed meals and returns it. Or creates new lunch and enclosed meal if lunch with specified id does not exists

####Parameters: id - integer

#### Body: lunch object with meals that needs to be updated

Example Value:
```json
{
  "id": 100015,
  "restaurantName": "restaurant_test_updated",
  "meals": [
    {
      "id": 100016,
      "dishName": "dish_test_2_updated",
      "price": 400
    }
  ]
}
```

####Responses:
**Code: 200** (OK)

Example Value:
```json
{
  "id": 100015,
  "created": "2017-08-14",
  "restaurantName": "restaurant_test_updated",
  "meals": [
    {
      "id": 100016,
      "created": "2017-08-14",
      "dishName": "dish_test_2_updated",
      "price": 400
    }
  ]
}
```
```
for linux:
curl -s -X PUT -d '{"id": 100015, "created": "2017-08-14", "restaurantName": "restaurant_test_updated", "meals": [{"id": 100016, "created": "2017-08-14", "dishName": "dish_test_2_updated", "price": 400}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/lunches/100015 --user admin@gmail.com:adminpass
```
```
for windows:
curl -s -X PUT -d "{\"id\": 100015, \"created\": \"2017-08-14\", \"restaurantName\": \"restaurant_test_updated\", \"meals\": [{\"id\": 100016, \"created\": \"2017-08-14\", \"dishName\": \"dish_test_2_updated\", \"price\": 400}]}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/lunches/100015 --user admin@gmail.com:adminpass
```

**Code: 422** (Unprocessable Entity) - like when POST or when for lunch already voted and it can not be changed

**Code: 409** (Conflict) - like when POST

**Code: 500** (Internal Server Error)

Returns if id of updating lunch specified in body (json) not match with id specified in url

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/lunches/100015",
  "cause": "IllegalArgumentException",
  "details": [
    "Lunch{id=100055, created=2017-08-14, restaurantName='restaurant_test_updated', meals=[Meal{id=100016, created=2017-08-14, dishName='dish_test_2_updated', price=400}]} must be with id=100015"
  ]
}
```
```
for linux:
curl -s -X PUT -d '{"id": 100055, "created": "2017-08-14", "restaurantName": "restaurant_test_updated", "meals": [{"id": 100016, "created": "2017-08-14", "dishName": "dish_test_2_updated", "price": 400}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/voting/rest/lunches/100015 --user admin@gmail.com:adminpass
```
```
for windows:
curl -s -X PUT -d "{\"id\": 100055, \"created\": \"2017-08-14\", \"restaurantName\": \"restaurant_test_updated\", \"meals\": [{\"id\": 100016, \"created\": \"2017-08-14\", \"dishName\": \"dish_test_2_updated\", \"price\": 400}]}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/voting/rest/lunches/100015 --user admin@gmail.com:adminpass
```

### DELETE /voting/rest/lunches/{id}
Deletes a single lunch by id

####Parameters: id - integer 

#### Body: No body

####Responses:
**Code: 200** (OK)

```
curl -s -X DELETE http://localhost:8080/voting/rest/lunches/100015 --user admin@gmail.com:adminpass
```

**Code: 422** (Unprocessable Entity)

Returns if lunch with specified id not found or for lunch already voted and it can not be changed

Example Value:
```json
{
  "url": "http://localhost:8080/voting/rest/lunches/100088",
  "cause": "NotFoundException",
  "details": [
    "The lunch with such id doesn't exist"
  ]
}
```
```
curl -s -X DELETE http://localhost:8080/voting/rest/lunches/123 --user admin@gmail.com:adminpass
```

**Code: 405** (Method Not Allowed)

Returns if id not specified in url

```
curl -s -X DELETE http://localhost:8080/voting/rest/lunches --user admin@gmail.com:adminpass
```

###General responses:
- Code: 401 Unauthorized
- Code: 403 Forbidden - returns if authorized user is not admin