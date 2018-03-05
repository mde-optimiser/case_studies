args <- commandArgs(trailingOnly = TRUE)

library(foreach)
library(leaps)
library(earth)
library(DAAG)
library(e1071)

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

	# Response Variables: Delay	Delay2	Latency	Latency2	Success_Ratio	Energy
	responses <- c(2, 4, 5, 6)
	responses_names <- c("Delay2", "Latency2", "Success_Ratio", "Energy")
	responses_names2 <- c("Response_Time", "Network_Latency", "Success_Ratio", "Energy")

	# Explanatory Variables: Hops	txPackets		LongestPath	Orchestrators	Neighbors	Distance
	predictors <- c(7, 10, 14, 15, 16, 17, 18, 19)
	predictors_names <- c("Hops",  "Orchestrators", "DevFast", "DevMedium", "DevSlow", "LoadSmall", "LoadMedium", "LoadBig")

##
## Step 2: Variable transformation
##

	# Leave untransformed: Orchestrators (10) and Distance (13)
	# Moderately positive skewness
	## Hops = log(Hops)
	data_total[,7] = log(data_total[,7])
	
	# LongesthPath = log(LongesthPath)
	data_total[,9] = log(data_total[,9])

	# Neighbors = log(Neighbors)	
	data_total[,11] = log(data_total[,11])
	
	# Substantially negatively skewed
	# txPackets = hist(sqrt(max(txPackets) + 1 - txPackets))
	data_total[,8] = sqrt(max(data_total[,8]) + 1 - data_total[,8])

	# Paths = hist(sqrt(max(Paths) + 1 - Paths))
	data_total[,12] = sqrt(max(data_total[,12]) + 1 - data_total[,12])

	## 1st Choice
	# Brute Force:
	# Optimal Parameters found in a previous call:
	# best_formula_valid <-  c(100,  34,  67, 105)

	## 2nd Choice
	# By observing the graphs RMSE vs. model complexity
	#best_formula_valid <-  c(1,  77,  67, 108) # Ignore response time
	#best_parameters_valid <- matrix( c(24, 24, 21, 24, 0.004, 0.004, 0.003, 0.001, 16, 16, 16, 17), nrow=4, ncol =3)
	
	best_parameters_valid <- matrix( c(15, 15, 25, 15, 0.005, 0.003, 0.004, 0.005, 18, 22, 21, 22), nrow=4, ncol =3)
	best_formula_valid <- c(241, 122, 183, 164)

	xcomb <- foreach(i=1:length(predictors), .combine=c) %do% {combn(predictors_names, i, simplify=FALSE) }

	optimal_nk = as.numeric(best_parameters_valid[1,1])
	optimal_thresh = as.numeric(best_parameters_valid[1,2])
	optimal_nprune = as.numeric(best_parameters_valid[1,3])
	formlist <- lapply(xcomb, function(l) formula(paste(responses_names[1], paste(l, collapse="+"), sep="~")))
	modelQoS1 <- earth(formula(formlist[[best_formula_valid[1]]]), data = data_total, nk = optimal_nk, thresh = optimal_thresh, nprune = optimal_nprune)
	
	optimal_nk = as.numeric(best_parameters_valid[3,1])
	optimal_thresh = as.numeric(best_parameters_valid[3,2])
	optimal_nprune = as.numeric(best_parameters_valid[3,3])
	formlist <- lapply(xcomb, function(l) formula(paste(responses_names[3], paste(l, collapse="+"), sep="~")))
	modelQoS2 <- earth(formula(formlist[[best_formula_valid[3]]]), data = data_total, nk = optimal_nk, thresh = optimal_thresh, nprune = optimal_nprune)
	
	optimal_nk = as.numeric(best_parameters_valid[4,1])
	optimal_thresh = as.numeric(best_parameters_valid[4,2])
	optimal_nprune = as.numeric(best_parameters_valid[4,3])
	formlist <- lapply(xcomb, function(l) formula(paste(responses_names[4], paste(l, collapse="+"), sep="~")))
	modelQoS3 <- earth(formula(formlist[[best_formula_valid[4]]]), data = data_total, nk = optimal_nk, thresh = optimal_thresh, nprune = optimal_nprune)