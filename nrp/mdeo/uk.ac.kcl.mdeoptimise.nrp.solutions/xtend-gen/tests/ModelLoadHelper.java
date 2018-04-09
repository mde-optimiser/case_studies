package tests;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;

@SuppressWarnings("all")
public class ModelLoadHelper {
  public static EObject loadModel(final String filename) {
    EObject _xblockexpression = null;
    {
      final HenshinResourceSet resourceSet = new HenshinResourceSet("src/tests/testModels");
      final String metamodelPath = "../../models/nrp/nextReleaseProblem.ecore";
      resourceSet.registerDynamicEPackages(metamodelPath).get(0);
      _xblockexpression = resourceSet.getResource(filename).getContents().get(0);
    }
    return _xblockexpression;
  }
}
