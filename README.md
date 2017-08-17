# Voting for lunch
Voting system for deciding where to have lunch.

# Task
- 2 types of users: admin and regular users
- Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
- Menu changes each day (admins do the updates)
- Users can vote on which restaurant they want to have lunch at
- Only one vote counted per user
- If user votes again the same day:
  - If it is before 11:00 we asume that he changed his mind.
  - If it is after 11:00 then it is too late, vote can't be changed
- Each restaurant provides new menu each day.

# API documentation and curl commands to test it
- **[Lunches](./LunchesDoc.md)**
- **[Votes](./VotesDoc.md)**

# Questions and possible improvements and additions
- Requires the development of logic related to the ability to delete lunches, users and voices, adding an "activity" field for entries
- Implement a restaurant in the form of a full-fledged entity that can have a lot of data (name, contacts, work schedule, photos, etc.)
- Expand the list of methods for working with lunches (filtering, sorting, paging, etc.)
- It is possible to switch to the Spring Data JPA and add caching at the service level (depending on the scenarios)
- Add controllers for users
- Add localization
- Add processing 401 and 430, as well as other possible exceptions, so that they return as json