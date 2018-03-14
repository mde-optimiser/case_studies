args <- commandArgs(trailingOnly = TRUE)

library(foreach)
library(e1071)
library(languageR)
library(party)
library(randomForest)
library(MASS)

##
## Read,transform, and split data
##

	set.seed(1)

##
## Step 1: Read train data
##

	data_total <- read.csv(path, sep="\t", header=T)
	#data_total <- data_total[sample(nrow(data_total), sample_size), ] # Randomly choose 1000 data points
	data_total$ID <- NULL			# Delete ID column
	data_total$ShortestPath <- NULL	# Delete ShortestPath column
	data_total$rxPackets <- NULL		# Delete rxPackets column
	data_total$txPackets <- NULL		# Delete rxPackets column
	data_total$LongestPath <- NULL		# Delete rxPackets column
	data_total$Neighbors <- NULL		# Delete rxPackets column
	data_total$Distance <- NULL		# Delete rxPackets column
	data_total$Paths <- NULL		# Delete rxPackets column

	# Response Variables: Delay	Delay2	Latency	Latency2	Success_Ratio	Energy
	responses <- c(2, 4, 5, 6)
	responses_names <- c("Delay2", "Latency2", "Success_Ratio", "Energy")
	responses_names2 <- c("Response_Time", "Network_Latency", "Success_Ratio", "Energy")

	# Explanatory Variables: Hops	txPackets		LongestPath	Orchestrators	Neighbors	Distance
	predictors <- c(7, 8, 9, 10, 11, 12, 13, 14)
	predictors_names <- c("Hops",  "Orchestrators", "DevFast", "DevMedium", "DevSlow", "LoadSmall", "LoadMedium", "LoadBig")

	## 2nd Choice
	# By observing the graphs RMSE vs. model complexity
	#best_parameters <- matrix( c(2000, 500, 1000, 3000, 1, 3, 1, 1), nrow = 4, ncol = 2)
	#best_formula <- c(30, 66, 67, 122)
  	best_parameters <- matrix( c(3000, 3000, 2000, 3000, 2, 1, 5, 1), nrow = 4, ncol = 2)
  	best_formula <- c(166, 195, 37, 18)

	xcomb <- foreach(i=1:length(predictors), .combine=c) %do% {combn(predictors_names, i, simplify=FALSE) }

	optimal_ntree = as.numeric(best_parameters[1, 1])
	optimal_mtry = as.numeric(best_parameters[1, 2])
	# Train the best model with the Training Set
	modelQoS1 <- randomForest(data_total[, 7:14], data_total[,responses[1]], ntree = optimal_ntree, mtry = optimal_mtry, importance=TRUE)
	
	optimal_ntree = as.numeric(best_parameters[3, 1])
	optimal_mtry = as.numeric(best_parameters[3, 2])
	# Train the best model with the Training Set
	modelQoS2 <- randomForest(data_total[, 7:14], data_total[,responses[3]], ntree = optimal_ntree, mtry = optimal_mtry, importance=TRUE)
	
	optimal_ntree = as.numeric(best_parameters[4, 1])
	optimal_mtry = as.numeric(best_parameters[4, 2])
	# Train the best model with the Training Set
	modelQoS3 <- randomForest(data_total[, 7:14], data_total[,responses[4]], ntree = optimal_ntree, mtry = optimal_mtry, importance=TRUE)