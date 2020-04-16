# CSYE7200-Final
<img src="https://img.shields.io/badge/build-Success-green"> <img src="https://img.shields.io/badge/Version-1.0.0-orange"> <img src="https://img.shields.io/badge/test-pass-brightgreen">

## 1. Abstract

- Design a concept named Technology Stock Index which can measure the risk and investment value of the technology stocks

- Estimating the risk of a portfolio investment and provide reference for stocks investor

- nvestor can input the portfolio investment and our system will generate risk expectations for these stocks and the values of Technology Stock Index

- The System will recommend you that whether this combination is worth

## 2. Methodology

- We are going to collect daily stocks data of 8 important technology companies during 3+ yrs. Parse Data and make data cleaning(ETL process)
Design and implement the algorithm models(For single company)

- Design and implement the model for Technology Stock Index
 - Data cleaning: parse, filter and transform
 - Value of Risk Algorithm Model
 - Technology Stock Index Algorithm Model
 - Result Integration and Visualization

- Train the models on Spark
 - Package the project as `.jar` file
 - run `spark-submit` command to perform the project

- Implement the result integration and visualization
 - use `Redis` to cache the data
 - Use `Flask` to implement the Web Application

## 3.How to Run

- Download the Spark(Prebuild for Hadoop) `version:2.4.5`: [Spark Download Link](https://spark.apache.org/downloads.html?S_TACT=100DY3BW)

- Git clone the repository:

		git clone git@github.com:MrYuan123/CSYE7200-Final.git
		
- `cd` into the project folder `7200_Final_TechIndex`:

		cd CSYE7200-Final
		cd 7200_Final_TechIndex
		
- Build the `.jar` file, and you will get `.jar` file in the `/target/scala-2.11`:

		sbt package
		
- unzip the Spark package, go to the bin folder, run:

		spark-submit /(projectpath)/7200_Final_TechIndex-2.11-1.0.jar

- Then you will see the result

## 4.Data Visualization
- Build Web Server of a search engine by Flask framework and cache data in Redis. Technology Stock Index, 8 stocks and corresponding indexes/factors have stored in database.  
- Run the application, then you can see the following page. You can insert the single stock name or portfolio.

![Search Result](https://github.com/MrYuan123/CSYE7200-Final/blob/Ran_Zhou_Branch/Result_Images/Result1.png)

- Then, the results will show in table. The results include VaR(Value of Risk), Recommendation(Recommended or not). The following image shows the combination of "BABA", "ORCL", "IBM", and risk of this portfolio is lower than Index, so it is recommended.

![Search Result](https://github.com/MrYuan123/CSYE7200-Final/blob/Ran_Zhou_Branch/Result_Images/Result2.png)

- The following image is the result of portfolio of "APPL", "GOOG", "AMZN", and it is not recommended.

![Search Result](https://github.com/MrYuan123/CSYE7200-Final/blob/Ran_Zhou_Branch/Result_Images/Result3.png)