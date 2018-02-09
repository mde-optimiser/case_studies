import org.dynemf.EObjectWrapper;
import org.dynemf.ResourceSetWrapper;
import org.dynemf.ResourceWrapper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.io.*;

import static org.dynemf.ResourceSetWrapper.rset;

public class SolutionsQualityEvaluator {

    public static void main(String[] args) throws IOException {

        SolutionsQualityEvaluator evaluator = new SolutionsQualityEvaluator();

//        if(args.length == 1){
//          System.out.println("Evaluating solutions quality for path: " + args[0]);
//            evaluator.evaluate(args[0]);
            evaluator.evaluate("./batch-0/");
//        } else {
//            System.out.println("Expecting a valid path containing MDEO solutions:");
//        }
    }

    public void evaluate(String solutionsPath) throws IOException {

        File[] solutionModelPaths = getSolutionsModelFiles(solutionsPath);

        for(File solutionModelPath : solutionModelPaths){
            EObjectWrapper<EObject> solution = loadSolutionModel(solutionModelPath);

            saveSolutionEvaluation(solution, solutionModelPath);
        }
    }

    private void saveSolutionEvaluation(EObjectWrapper solution, File solutionModelPath) throws IOException {

        File evaluationOutput = new File(solutionModelPath.getAbsolutePath() + ".evaluation.txt");

        PrintWriter sw = new PrintWriter(new FileOutputStream(evaluationOutput, false));
        evaluateSolutionSprints(solution, sw);
        sw.flush();
    }

    private void evaluateSolutionSprints(EObjectWrapper solution, PrintWriter out) {

        EList<EObject> sprints = getFeatureList((EObject) solution.result(), "sprints");
        out.println();
        out.println(String.format("Found %s sprints", sprints.size()));

        evaluateBacklogSize(solution, out);

        //stakeholders
            // items
            // effort
            //importance

        for(int i = 0; i < sprints.size(); i++){
            evaluateSprint(sprints.get(i), i, out);
            out.println();
            out.println("=============================================");
            out.println();
        }

    }

    private void evaluateBacklogSize(EObjectWrapper solution, PrintWriter out) {

        EObject backlog = getFeatureObject((EObject) solution.result(), "backlog");
        EList<EObject> workItems = getFeatureList(backlog, "workitems");

        int backlogEfort = 0;

        for(EObject workItem : workItems){
            backlogEfort += getFeatureInt(workItem, "Effort");
        }

        out.println(String.format("Backlog items: %s", workItems.size()));
        out.println(String.format("Backlog effort points size: %s", backlogEfort));

        out.println();
        out.println();
    }

    private void evaluateSprint(EObject sprint, int index, PrintWriter out) {


        out.println(String.format("Sprint %s stats", index));
        out.println();
        out.println("---------------------------------------------");
        out.println();
        EList<EObject> workItems = getFeatureList(sprint, "committedItem");

        out.println(String.format("Committed items total: %s", workItems.size()));

        int sprintEffort = 0;

        for(EObject workItem : workItems){
            sprintEffort += getFeatureInt(workItem, "Effort");
        }

        out.println(String.format("Committed sprint effort: %s", sprintEffort));

        out.println();

        for(int i = 0; i < workItems.size(); i++) {
            int effort = getFeatureInt(workItems.get(i), "Effort");
            int importance = getFeatureInt(workItems.get(i), "Importance");
            out.println(String.format("Committed Work Item %s, Effort %s, Importance %s", i, effort, importance));
        }

    }

    private EObjectWrapper<EObject> loadSolutionModel(File solutionPath) {

        // Register the metamodel
        ResourceSetWrapper rset = rset().register("src/main/resources/planning.ecore");

        // Load an existing model that conforms to the previously loaded metamodel
        ResourceWrapper resourceWrapper = rset.open(solutionPath.getAbsolutePath());

        return (EObjectWrapper<EObject>) resourceWrapper.root();
    }

    private File[] getSolutionsModelFiles(String solutionsPath){

        File solutionsOutputPath = new File(solutionsPath);

        //Check the path
        if(solutionsOutputPath.isDirectory() && solutionsOutputPath.exists()){
            System.out.println(String.format("Invalid solutions path given: %s", solutionsPath));
        }

        //Check that it contains at least one xmi file
        return solutionsOutputPath.listFiles(new FilenameFilter(){
            public boolean accept(File dir, String name) {
                return name.endsWith(".xmi");
            }
        });

    }

    /**
     * Helper function getting the value of the named feature (if it exists) for the given EObject.
     */
    private Object getFeature (EObject o, String feature) {

        if(o == null){
            System.out.println("Null object given");
        }

        return o.eGet(o.eClass().getEStructuralFeature(feature));
    }

    private EList<EObject> getFeatureList(EObject object, String feature) {
        return (EList<EObject>) getFeature(object, feature);
    }

    private EObject getFeatureObject(EObject object, String feature) {
        return (EObject) getFeature(object, feature);
    }

    private int getFeatureInt(EObject object, String feature) {
        return (Integer) getFeature(object, feature);
    }
}