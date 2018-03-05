args <- commandArgs(trailingOnly = TRUE)

library(foreach)
library(e1071)
library(rpart)
library(caret)

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
	
	#best_formula <-  c(1,  41,  51, 55) # Ignore response time
	#best_parameters <- matrix( c(50, 50, 100, 100, 0.015, 0.015, 0.005, 0.005), nrow=4, ncol =2)
  	best_formula <- c(166, 66, 78, 165)
	best_parameters <- matrix( c(100, 50, 100, 50, 0.001, 0.015, 0.005, 0.001), nrow=4, ncol =2)
	
	xcomb <- foreach(i=1:length(predictors), .combine=c) %do% {combn(predictors_names, i, simplify=FALSE) }

	optimal_minsplit = as.numeric(best_parameters[1, 1])
	optimal_cp = as.numeric(best_parameters[1, 2])
	control <- rpart.control(cp = optimal_cp,  minsplit = optimal_minsplit)
	modelQoS1 <- train(data_total[, 7:14], data_total[,responses[1]], method = "rpart", control=rpart.control(minsplit=2), trControl = trainControl(method = "cv", number = 10),)

	optimal_minsplit = as.numeric(best_parameters[3, 1])
	optimal_cp = as.numeric(best_parameters[3, 2])
	control <- rpart.control(cp = optimal_cp,  minsplit = optimal_minsplit)
	modelQoS2 <- train(data_total[, 7:14], data_total[,responses[3]], method = "rpart", control=rpart.control(minsplit=2), trControl = trainControl(method = "cv", number = 10),)

	optimal_minsplit = as.numeric(best_parameters[4, 1])
	optimal_cp = as.numeric(best_parameters[4, 2])
	control <- rpart.control(cp = optimal_cp,  minsplit = optimal_minsplit)
	modelQoS3 <- train(data_total[, 7:14], data_total[,responses[4]], method = "rpart", control=rpart.control(minsplit=2), trControl = trainControl(method = "cv", number = 10),)
