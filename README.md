# CSYE7200-Final

## 1.How to Run

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
		
