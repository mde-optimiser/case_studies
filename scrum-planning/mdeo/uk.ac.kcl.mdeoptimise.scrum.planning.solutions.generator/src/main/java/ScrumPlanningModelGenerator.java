/**
 * Created by alxbrd on 2/7/18.
 */

import org.dynemf.EObjectWrapper;
import org.dynemf.EPackageWrapper;
import org.dynemf.ResourceSetWrapper;
import org.eclipse.emf.ecore.EObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.dynemf.ResourceSetWrapper.rset;

public class ScrumPlanningModelGenerator {

    public static void main(String args[]) throws IOException {

        ResourceSetWrapper rset = rset().register("src/main/resources/planning.ecore");

        EPackageWrapper mm = rset.ePackage("http://www.kcl.ac.uk/mdeoptimiser/sprint-planning");

        EObjectWrapper<EObject> a = mm.create("Plan");

        generateRandomStakeholders(50, a, mm);

        rset.create("gen/sprint-planning-model.xmi").add(a).save();
    }

    public static void generateRandomStakeholders(int maximumStakeholders, EObjectWrapper<EObject> model, EPackageWrapper metamodel){

        int stakeholders = new Random().nextInt(maximumStakeholders) + 1;

        EObjectWrapper<EObject> backlog = metamodel.create("Backlog");

        model.set("backlog", backlog);

        for(int i =0; i < stakeholders; i++) {

            EObjectWrapper stakeholder = metamodel.create("Stakeholder");

            generateRandomWorkItems(50, backlog, stakeholder, metamodel);

            model.add("stakeholders", stakeholder);
        }
    }

    public static void generateRandomWorkItems(int maxWorkItems, EObjectWrapper<EObject> backlog, EObjectWrapper stakeholder, EPackageWrapper metamodel) {
        int stakeholderWorkItems = new Random().nextInt(maxWorkItems) + 1;

        List<Integer> complexities = new ArrayList<Integer>();
        complexities.add(1);
        complexities.add(3);
        complexities.add(5);
        complexities.add(8);

        List<Integer> importances = new ArrayList<Integer>();
        importances.add(1);
        importances.add(3);
        importances.add(5);
        importances.add(8);

        List<EObjectWrapper<EObject>> workItems = new ArrayList<EObjectWrapper<EObject>>();

        for(int i =0; i < stakeholderWorkItems; i++){

            int importance = new Random().nextInt(importances.size());
            int complexity = new Random().nextInt(complexities.size());

            EObjectWrapper workItem = metamodel.create("WorkItem");
            workItem.set("Importance", importances.get(importance));
            workItem.set("Effort", complexities.get(complexity));
            workItem.set("stakeholder", stakeholder);
            workItems.add(workItem);

            backlog.add("workitems", workItem);
        }

    }

}
