# BeerOrder

<a href = 'https://www.hackerearth.com/challenge/hiring/thoughtworks-mobile-developer-hiring-challenge-1/'>Contest held on HackerEarth </a>
<br>


Fast Pagination with auto-suggest search and filter functionality implemented

Utilized JSON Response Parameters from url through Retrofit
Implement functionality to list beers in a mobile app
<br>
Search beer by name
<br>
 Sort feature to sort result through ‘alcohol content’ in ascending and descending order
 <br>
Filter feature to filter result by Beer style or FAVOURITE 
<br>
 Implemented Shopping Cart to allow users to add the beers into their cart. Maintained state of the cart across sessions through sqlite.
<br>Custom Font and Icon are used to make app interactive.
<br>
Added autocomplete feature to Search beers conveniently 
<br>
Features I have included extra :<br>
PAGINATION to display 15 beer items at a time in one page as user scrolls next page is loaded from list. This saves the memory usage.
Offline storage of beer items through sqlite to browse even without internet. Only those items to which user has scrolled are stored offline.
<br>
Favorite option to add beer to favorite list and see it by applying favorite filter present on filter list at top.
<br>
Filter list is initiated dynamically from received json to list all styles.
<br>
Search is optimized if user sets filter of particular style and then tries to search any beer then system searches only in list which satisfies filter setting,
<br>
Sort can be applied to any nested level like after applying some filter and typing search text then we can sort the resulted item in either increasing and decreasing order of alcohol content.
<br>
Removal of any item added to cart can be done and cart is maintained offline through sqlite.
<br>
INSTRUCTIONS :


App is built to be easily understand any function.

We need to have internet connection to load data.
Items can be added to favorite by clicking heart button.
Items can be added to cart by clicking cart button and increasing quantity on next page where all details about item is displayed .
At top search can be done by typing text.
Cart items can be viewed by clicking cart button at top.
Items from cart can be removed by clicking garbage button beside them.
Dropdown Spinner can be used to select sort or filter options.


This app has been tested on Redmi 3s prime API 23.
<br>
Screenshots
 <p align = 'center'>
 <img  width = '200' height = '300' src = 'https://github.com/kushagrasaxenaknit/BeerOrder/blob/master/Screenshots/Screenshot_2018-07-01-17-10-02-264_com.kushagra.beerorder.png' />
  <img width = '200' height = '300' src = 'https://github.com/kushagrasaxenaknit/BeerOrder/blob/master/Screenshots/Screenshot_2018-07-01-17-10-16-095_com.kushagra.beerorder.png'/>
     <img  width = '200' height = '300' src = 'https://github.com/kushagrasaxenaknit/BeerOrder/blob/master/Screenshots/Screenshot_2018-07-01-17-10-40-654_com.kushagra.beerorder.png' />
  <img width = '200' height = '300' src = 'https://github.com/kushagrasaxenaknit/BeerOrder/blob/master/Screenshots/Screenshot_2018-07-21-11-24-16-494_com.kushagra.beerorder.png'/>

   
 </p>
 <br>
See Demo Video <a href = 'https://github.com/kushagrasaxenaknit/BeerOrder/blob/master/Screenshots/Demo%20Video.mp4'>here </a>
<br>
Download Demo Version <a href = 'https://github.com/kushagrasaxenaknit/BeerOrder/blob/master/Screenshots/kushagraBeer.apk'>here </a>

