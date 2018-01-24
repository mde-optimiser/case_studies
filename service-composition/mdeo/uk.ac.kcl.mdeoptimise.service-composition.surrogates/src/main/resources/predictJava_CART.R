####################################################################################################################
##
## Linear Regression (LR)
##

args <- commandArgs(trailingOnly = TRUE)

#
# Calculates the QoS of a candidate composition configuration based on each predictor variables.
#
# INPUT:
# configuration : The values of the predictor variables of the candidate configuration
#
predictQoS_LR <- function(configuration) {

	library(foreach)
	library(e1071)
	library(rpart)
	library(caret)

	##
	## Read,transform, and split data
	##

	set.seed(1)

	graphs_path <- "images"
	display_graphs <- 0
	sample_size <- 1000

	##
	## Step 1: Read train data
	##

		data_total <- read.csv("/home/eustathi/Regression/Datasets/scenario4-TrainingSet/allConfigurations.csv", sep="\t", header=T)
		#cat("Initial size of total data = ", dim(data_total), "\n")	
		data_total <- data_total[sample(nrow(data_total), sample_size), ] # Randomly choose 1000 data points
		#cat("Sampled size of total data = ", dim(data_total), "\n")

		data_total$ID <- NULL			# Delete ID column
		data_total$ShortestPath <- NULL	# Delete ShortestPath column
		data_total$rxPackets <- NULL		# Delete rxPackets column

		# Response Variables: Delay	Delay2	Latency	Latency2	Success_Ratio	Energy
		responses <- c(2, 4, 5, 6)
		responses_names <- c("Delay2", "Latency2", "Success_Ratio", "Energy")
		responses_names2 <- c("Response_Time", "Network_Latency", "Success_Ratio", "Energy")
	
		# Explanatory Variables: Hops	txPackets		LongestPath	Orchestrators	Neighbors	Distance
		predictors <- c(7, 8, 9, 10, 11, 12, 13)
		predictors_names <- c("Hops", "txPackets", "LongestPath", "Orchestrators", "Neighbors", "Paths", "Distance")

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
	best_parameters <- matrix( c(50, 50, 50, 50, 0.005, 0.01, 0.01, 0.005), nrow=4, ncol =2)
	best_formula <- c(109, 125, 74, 54)

	xcomb <- foreach(i=1:length(predictors), .combine=c) %do% {combn(predictors_names, i, simplify=FALSE) }

	QoSMetrics <- c()

	for(j in 1:length(responses)) {
		optimal_minsplit = as.numeric(best_parameters[j, 1])
		optimal_cp = as.numeric(best_parameters[j, 2])

		# Train the best model with the Training Set
		control <- rpart.control(cp = optimal_cp,  minsplit = optimal_minsplit)
		cart <- train(data_total[, 7:13], data_total[,responses[j]], method = "rpart", control=rpart.control(minsplit=2), trControl = trainControl(method = "cv", number = 10),)

		#print(lr)
		#print(formlist[[best_formula_valid[j]]])

		cart_pred <- predict(cart, newdata = data_candidate)
		# The predicted value	
		#print(lr_pred)
		QoSMetrics <- c(QoSMetrics, as.matrix(cart_pred)[1])
		# The original value
		#print(configuration[,responses[j]])
	}
	
  list(result1 = QoSMetrics)
}


##
## Step 1: Read data
##
	data_candidate <- read.csv(paste(args, sep=""), sep="\t", header=T)
	#data_candidate <- read.csv(paste("/home/eustathi/Regression/Datasets/Java/test.csv", sep=""), sep="\t", header=T)
	data_candidate$ID <- NULL			# Delete ID column
	data_candidate$ShortestPath <- NULL		# Delete ShortestPath column
	data_candidate$rxPackets <- NULL		# Delete rxPackets column

##
## Step 2: Variable transformation
##
	# Leave untransformed: Orchestrators (10) and Distance (13)
	# Moderately positive skewness
	## Hops = log(Hops)
	data_candidate[,7] = log(data_candidate[,7])
	
	# LongesthPath = log(LongesthPath)
	data_candidate[,9] = log(data_candidate[,9])

	# Neighbors = log(Neighbors)	
	data_candidate[,11] = log(data_candidate[,11])
		
	# Substantially negatively skewed
	# txPackets = hist(sqrt(max(txPackets) + 1 - txPackets))
	data_candidate[,8] = sqrt(max(data_candidate[,8]) + 1 - data_candidate[,8])

	# Paths = hist(sqrt(max(Paths) + 1 - Paths))
	data_candidate[,12] = sqrt(max(data_candidate[,12]) + 1 - data_candidate[,12])

#print(data_candidate)
results_candidate = predictQoS_LR(data_candidate)
print(results_candidate)
# predictQoS_LR(data_test[1,])

##
## End of Linear Regression (LR)
##
####################################################################################################################
