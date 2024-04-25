## Final Case Project: Getir Android Shopping App

This project aims to develop a shopping app similar to Getir  app. Users will be able to list products they can shop, view product details, and add/remove items to/from their carts. This document outlines the technical details and requirements of the project.

## App Demo
https://github.com/SelenSonmez/Getir_Bootcamp_Final_Case/assets/71898275/1524e80f-44ab-4a21-9b5c-315717debcbd

## Screenshots
<p align="center">
<img src="https://github.com/SelenSonmez/Getir_Bootcamp_Final_Case/assets/71898275/ec4e3b52-bf0f-42d8-9ab9-ef408cce7d35" width="25%"></img> 
<img src="https://github.com/SelenSonmez/Getir_Bootcamp_Final_Case/assets/71898275/af8e7396-5094-41c1-9be8-a215e39b76a6" width="25%"></img> 
<img src="https://github.com/SelenSonmez/Getir_Bootcamp_Final_Case/assets/71898275/42b4909e-95e6-4b0a-b3df-e04f2a925182)" width="25%"></img> 
<img src="https://github.com/SelenSonmez/Getir_Bootcamp_Final_Case/assets/71898275/8341684e-b534-4424-9ca8-4494ea51d96c" width="25%"></img> 
<img src="https://github.com/SelenSonmez/Getir_Bootcamp_Final_Case/assets/71898275/4f10e05b-fd72-4dc6-9880-46319eada763" width="25%"></img> 
</p>

## Tech Stack

- Kotlin.
- MVVM architecture.
- Flow and Coroutines.
- Hilt

## Libraries 
- Retrofit
- Glide
- Room
- Gson

### Product Listing Screen

- The app fetches and display data from the provided mock APIs.
- Horizontal and vertical scrollable lists of products are displayed simultaneously.
- Users can click on products to navigate to the Details screen.
- If there are any items in the cart, the total amount is displayed on the screen.
- Users can navigate to the Cart Screen from the Product Listing screen.

### Details Screen

- The detailed image, name, price, description, and current amount in the cart of the product are displayed.
- Users can update the quantity of products in the cart and save it by clicking the stepper.

### Cart Screen

- Users are able to see all items and their individual counts in the cart listed correctly.
- When users click the Checkout button, a success message should be displayed on the screen.
- The success message contains the total cart amount.
- After the action of the message, all local data is reset, and the app return to the Product Listing Screen.
- Suggested products are displayed as horizontal view in cart screen.


