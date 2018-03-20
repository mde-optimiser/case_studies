/**
 * Created by alxbrd on 2/7/18.
 */

import com.github.javafaker.Faker;
import org.dynemf.EObjectWrapper;
import org.dynemf.EPackageWrapper;
import org.dynemf.ResourceSetWrapper;
import org.eclipse.emf.ecore.EObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.dynemf.ResourceSetWrapper.rset;

public class NRPModelGenerator {

    private List<EObjectWrapper> softwareArtifactObjects = new ArrayList<EObjectWrapper>();
    private List<EObjectWrapper> requirementRealisationObjects = new ArrayList<EObjectWrapper>();
    private List<EObjectWrapper> valuationObjects = new ArrayList<EObjectWrapper>();

    public static void main(String args[]) throws IOException {

        NRPModelGenerator modelGenerator = new NRPModelGenerator();
        modelGenerator.generateModel();
    }

    public void generateModel() throws IOException {
        ResourceSetWrapper rset = rset().register("src/main/resources/nextReleaseProblem.ecore");

        EPackageWrapper metamodel = rset.ePackage("http://model.nrp");

        EObjectWrapper<EObject> modelRoot = metamodel.create("NRP");
        Faker faker = new Faker();

        int maximumCustomers = 25;
        int customers = 5;//getRandom(maximumCustomers);
        int maximumCustomerImportance = 5;

        int maximumRequirements = 50;
        int requirements = 25;//getRandom(maximumRequirements) + customers;

        int maximumValuations = 5;
        int valuations = getRandom(maximumValuations);

        int maximumRequirementRealisations = 5;
        int requirementRealisations = getRandom(maximumRequirementRealisations);

        int maximumSoftwareArtifacts =100;
        int softwareArtifacts = getRandom(maximumSoftwareArtifacts);

        int maximumSoftwareArtifactsDependencies = 5;

        //Generate customers
        List<EObjectWrapper> customerObjects = new ArrayList<EObjectWrapper>();
        for(int i = 0;  i < customers; i++) {

            EObjectWrapper customer = metamodel.create("Customer");
            customer.set("name", faker.name().fullName());
            customer.set("importance", (double) getRandom(maximumCustomerImportance));

            modelRoot.add("customers", customer);

            customerObjects.add(customer);
        }

        //Generate requirements
        for(int i = 0; i < requirements; i ++){
            EObjectWrapper requirement = metamodel.create("Requirement");

            requirement.set("name", String.format("Specification #%s", faker.code().ean8()));

            modelRoot.add("requirements", requirement);

            //Add Valuations
            //TODO randomise
            for(int j = 0; j < getRandom(valuations); j++){
                EObjectWrapper valuation = metamodel.create("Valuation");

                valuation.set("value", (double) getRandom(10));

                requirement.add("valuations", valuation);

                int  maximumValuationCustomers = getRandom(maximumValuations);

                for(int k = 0; k < maximumValuationCustomers; k++){

                    EObjectWrapper customer = customerObjects.get(getRandom(0, customerObjects.size()-1));
                        customer.add("assigns", valuation);
                }

                valuationObjects.add(valuation);
            }

            //Add Realisations
            //TODO randomise
            for(int j = 0; j < getRandom(requirementRealisations); j++){
                EObjectWrapper realisation = metamodel.create("RequirementRealisation");

                realisation.set("percentage", round(new Random().nextDouble(), 2));

                requirement.add("realisations", realisation);

                //Add at least one software artifact to each realization
                EObjectWrapper softwareArtifact = getSoftwareArtifact(metamodel, faker);
                realisation.add("dependsOn", softwareArtifact);

                softwareArtifactObjects.add(softwareArtifact);

                requirementRealisationObjects.add(realisation);
            }
        }

        //Generate SoftwareArtifacts
        for(int i = 0; i < softwareArtifacts; i++) {
            EObjectWrapper softwareArtifact = getSoftwareArtifact(metamodel, faker);

            //Build dependency tree on other artifacts for this artifact
            //TODO add multiple realisations randomly for a software artifact
            EObjectWrapper saRequirementRealisation = requirementRealisationObjects
                    .get(getRandom(0, requirementRealisationObjects.size() - 1));

            saRequirementRealisation.add("dependsOn", softwareArtifact);

            //Generate dependencies bool
            if (new Random().nextDouble() > 0.7) {

                //How many dependencies
                int dependencies = getRandom(maximumSoftwareArtifactsDependencies);

                //Generate them
                //TODO Make this recursive to make it more interesting
                for (int j = 0; j < dependencies; j++) {

                    EObjectWrapper dependencyArtifact = getSoftwareArtifact(metamodel, faker);
                    softwareArtifact.add("requires", dependencyArtifact);

                    //Add dependency on the same req realization as original sa
                    saRequirementRealisation.add("dependsOn", dependencyArtifact);

                    //Add another random realisation dependency for this artifact
                    EObjectWrapper dependencyReqRealisation = requirementRealisationObjects
                            .get(getRandom(0, requirementRealisationObjects.size() - 1));

                    dependencyReqRealisation.add("dependsOn", dependencyArtifact);

                    //modelRoot.add("availableArtifacts", dependencyArtifact);
                    softwareArtifactObjects.add(dependencyArtifact);
                }
            }

            softwareArtifactObjects.add(softwareArtifact);
        }

        for(EObjectWrapper softwareArtifact : softwareArtifactObjects){
            modelRoot.add("availableArtifacts", softwareArtifact);
        }

        //Finally add a solution

        EObjectWrapper solution = metamodel.create("Solution");

        modelRoot.add("solutions", solution);

        rset.create(String.format("gen/nrp/nrp-model-%s-cus-%s-req-%s-sa.xmi", customers,
                requirements, softwareArtifactObjects.size())).add(modelRoot).save();
        //rset.create(String.format("gen/nrp/nrp-model.xmi", customers, requirements)).add(modelRoot).save();
    }

    private EObjectWrapper getSoftwareArtifact(EPackageWrapper metamodel, Faker faker){

        EObjectWrapper softwareArtifact = metamodel.create("SoftwareArtifact");

        softwareArtifact.set("name", String.format("Software artifact %s version %s", faker.app().name(), faker.app().version()));

        EObjectWrapper cost = metamodel.create("Cost");
        cost.set("amount", (double) getRandom(50, 100));

        softwareArtifact.add("costs", cost);
        return softwareArtifact;
    }

    private int getRandom(int random) {
        return new Random().nextInt(random) + 1;
    }

    private int getRandom(int start, int end){
        return new Random().nextInt((end - start) + 1) + start;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}


94.78071884171639,55.92968070112384,0.5436204560223514
94.80033670928303,56.09615962740876,0.5436204560223514
73.51689987272049,61.44302310718278,0.5730936156997405
78.9536913822949,63.5168916942078,0.5873585419848657
92.0721320207125,56.09615962740876,0.5436204560223514
94.80033670928303,56.09615962740876,0.5436204560223514
94.80033670928303,56.09615962740876,0.5436204560223514
78.93407351472827,62.82972488591343,0.5975157635037852
73.51689987272049,64.20405850250216,0.5924371527443254
94.80033670928303,56.09615962740876,0.5436204560223514
78.93407351472827,62.82972488591343,0.5975157635037852
76.24510456129101,64.20405850250216,0.5873585419848657
78.93407351472827,62.82972488591343,0.5975157635037852
76.24510456129101,64.20405850250216,0.5873585419848657
116.54750274758072,57.23895321941457,0.5637257404047236
76.22548669372438,63.5168916942078,0.5873585419848657
68.08010836314605,65.41191319280597,0.5772013204659462
94.80033670928303,56.09615962740876,0.5436204560223514
94.80033670928303,56.09615962740876,0.5436204560223514
89.36354519970861,54.022291040383756,0.5293555297372262
94.78071884171639,55.92968070112384,0.5436204560223514
94.80033670928303,56.09615962740876,0.5436204560223514
78.93407351472827,62.82972488591343,0.5975157635037852
76.24510456129101,64.20405850250216,0.5873585419848657
64.65553523034431,64.89122531079653,0.5772013204659462
78.93407351472827,62.82972488591343,0.5975157635037852
73.51689987272049,64.20405850250216,0.5924371527443254
78.93407351472827,62.82972488591343,0.5975157635037852
73.51689987272049,64.20405850250216,0.5924371527443254
78.9536913822949,65.65851712871958,0.5933446676664705
68.06049049557943,61.44302310718278,0.5730936156997405
100.68123955302596,63.972518477919216,0.6074638263672378
89.80765653387711,63.59701670383647,0.5984232784259303
73.51689987272049,64.20405850250216,0.5822799312254059
78.93407351472827,62.82972488591343,0.5873585419848657
89.36354519970861,54.022291040383756,0.5293555297372262
78.93407351472827,62.82972488591343,0.5975157635037852
76.24510456129101,64.20405850250216,0.5873585419848657
110.62736416870453,48.50894863432481,0.49988237005983716
76.24510456129101,64.20405850250216,0.5873585419848657
78.93407351472827,62.82972488591343,0.5975157635037852
94.80033670928303,56.09615962740876,0.5436204560223514
104.9710632734151,60.37079154095048,0.5585164170810774
85.9357280615141,59.803704222688516,0.5520805308015929
110.38823691542288,58.99645792436174,0.5686736385999969
49.48564048002078,71.62479056930118,0.6412538494662995
54.942049857161834,73.76641600381299,0.6472399751479043
118.81197823441612,50.650574068836605,0.5007898849819823
124.26838761155717,52.40040945950808,0.4996732114043702
94.80033670928303,56.09615962740876,0.5436204560223514










