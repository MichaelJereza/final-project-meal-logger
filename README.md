# Meal Logger
Meal Logger is an application that allows the user to calculate the nutritional values of their meals by documenting what they eat.

It fetches data using the [USDA FoodData Central API](https://fdc.nal.usda.gov/api-guide.html).

## Meal Logger allows users to export their meals in the form of a nutrition label.
![alt text](https://github.com/MichaelJereza/final-project-meal-logger/blob/master/images/meal_label.png)

## The meal creation interface allows users to:
- Search food items and brands
- Specify portions
- Add a photo
- Create a title for the meal


![alt text](https://github.com/MichaelJereza/final-project-meal-logger/blob/master/images/img1.jpg)

## USDA FoodData Central contains information for specific brands and generic food items.

![alt text](https://github.com/MichaelJereza/final-project-meal-logger/blob/master/images/img2.jpg)


## Each added food items is calculated for the specified portion.
Depending on the API's availability there will be more specific serving size (1 medium breast).

![alt text](https://github.com/MichaelJereza/final-project-meal-logger/blob/master/images/img3.jpg)

## Personal Contribution
For this project I personally worked on the USDA API implementation. This involved two separate calls, one for fetching the query of foods, then using the selected food ID to fetch nutritional information.
I also designed the initial meal creation interface and the final food label exportation.