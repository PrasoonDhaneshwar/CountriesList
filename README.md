# Android Programming Test

## Objective
The goal of this challenge is to develop a simple Android application that displays a list of countries and their corresponding currency exchange rates to USD. The application should consist of two main screens:

### 1. Countries List Screen
- Display a list of all available countries.
- Each list item should include:
  - Country name
  - Country flag
  - Currency name
  - Currency symbol
- Tapping on an item should navigate to the second screen (Country Exchange Rates).
- Data source: [Countries and Currencies API](https://restcountries.com/v3.1/all)
- Not all countries have currency data available. Handle this case by displaying a placeholder message.

### 2. Country Exchange Rates Screen
- Display the exchange rate of the selected country's currency to USD.
- Data source: [Exchange Rate API](https://api.exchangerate-api.com/v4/latest/USD)
- Not all countries have exchange rate data available. Handle this case by displaying a placeholder message.

You are free to design the UI/UX, but the screens should include the elements mentioned above. The images below illustrate a basic layout concept (feel free to enhance it):

![Countries List Screen](screenshot%2Fscreen%201.png)
![Country Exchange Rates Screen](screenshot%2Fscreen%202.png)

## Technical Requirements
- The app should be built using **Kotlin** and support Android API level 24 or higher.
- Use any libraries or tools you find suitable.

## Provided Resources
- The API for countries is already implemented using Retrofit. Refer to the following classes:
  - CountriesApi: Defines the API endpoints for fetching country data. 
  - CountriesService: The service layer for interacting with the country API. 
  - CountriesResponse: The data model for parsing the country API response. 

- For the exchange rate API, only the interface and response model are provided. Feel free to implement the necessary functionality:
  - ExchangeRateApi: Interface for the exchange rate API endpoints. 
  - ExchangeRateResponse: The data model for parsing the exchange rate API response.

## Quality Expectations
- Focus on clean code and good architecture. Quality is key.
- Document your design choices, including any trade-offs or compromises due to time constraints.
- If you encounter limitations or areas that could be improved with more time, please note them in a README file.

## Time Allocation
- We suggest spending around 2 to 4 hours on this task, depending on your experience level.
- If you find yourself exceeding 6 hours, consider whether you are adding unnecessary complexity.
- Include a summary of your work, explaining what was prioritized and any TODOs for future improvements.

## Submission
- Create a private Git repository from this template and invite the following users as collaborators:
  - `@mohamadjaara`
  - `@vitorhugods`
- Please ensure the repository remains private and do not share your solution publicly or with third parties.

Good luck! We look forward to seeing your implementation.

## Notes and Implementation Details
Please use this section to document your thought process, decisions made during implementation, and any trade-offs or challenges encountered. Feel free to include details on:
- The architecture pattern you chose and why. 
- Any additional libraries or tools you used and their purpose. 
- Testing strategy and coverage details. 
- Any known issues, limitations, or areas for improvement.

