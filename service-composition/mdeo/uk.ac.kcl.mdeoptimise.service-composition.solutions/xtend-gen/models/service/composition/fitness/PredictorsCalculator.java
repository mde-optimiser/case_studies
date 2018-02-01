package models.service.composition.fitness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.henshin.model.resource.HenshinResourceSet;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class PredictorsCalculator {
  private static EObject predictors;
  
  public EObject getPredictors() {
    try {
      if ((PredictorsCalculator.predictors == null)) {
        HenshinResourceSet henshinResourceSet = new HenshinResourceSet("/home/alxbrd/projects/alxbrd/github/case_studies/service-composition/mdeo/uk.ac.kcl.mdeoptimise.service-composition.solutions/src/models/service/composition");
        final Resource resource = henshinResourceSet.createResource(URI.createURI("connected-nodes.xmi"));
        resource.load(Collections.EMPTY_MAP);
        PredictorsCalculator.predictors = resource.getAllContents().next();
      }
      return PredictorsCalculator.predictors;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public List<Integer> calculatePredictors(final EObject model) {
    ArrayList<Integer> predictors = new ArrayList<Integer>();
    predictors.add(Integer.valueOf(1));
    predictors.add(Integer.valueOf(this.calculateOrchestrators(model)));
    predictors.add(Integer.valueOf(this.calculateHops(model, this.getPredictors())));
    predictors.add(Integer.valueOf(this.countTypeNodes(1, model, this.getPredictors())));
    predictors.add(Integer.valueOf(this.countTypeNodes(2, model, this.getPredictors())));
    predictors.add(Integer.valueOf(this.countTypeNodes(3, model, this.getPredictors())));
    predictors.add(Integer.valueOf(this.countLoadNodes(1, model, this.getPredictors())));
    predictors.add(Integer.valueOf(this.countLoadNodes(2, model, this.getPredictors())));
    predictors.add(Integer.valueOf(this.countLoadNodes(3, model, this.getPredictors())));
    return predictors;
  }
  
  /**
   * Calculates the number of non-empty orchestrators in a concrete plan.
   */
  public int calculateOrchestrators(final EObject model) {
    Object _feature = this.getFeature(model, "concretePlans");
    EList<EObject> concretePlans = ((EList<EObject>) _feature);
    final Function2<Integer, EObject, Integer> _function = (Integer orchestratorsCount, EObject concretePlan) -> {
      Object _feature_1 = this.getFeature(concretePlan, "orchestrators");
      final Function1<EObject, Boolean> _function_1 = (EObject orchestrator) -> {
        Object _feature_2 = this.getFeature(orchestrator, "concreteServices");
        return Boolean.valueOf((_feature_2 != null));
      };
      int _size = IterableExtensions.<EObject>toList(IterableExtensions.<EObject>filter(((List<EObject>) _feature_1), _function_1)).size();
      return Integer.valueOf(((orchestratorsCount).intValue() + _size));
    };
    return (int) IterableExtensions.<EObject, Integer>fold(concretePlans, Integer.valueOf(0), _function);
  }
  
  /**
   * Calculates the total number of hops between the nodes within a service composition configuration.
   * Returns the total number of hops between a pair of nodes.
   */
  public int calculateHops(final EObject model, final EObject predictors) {
    int hops = 0;
    List<EObject> orchestrators = this.getFeatureList(IterableExtensions.<EObject>head(this.getFeatureList(model, "concretePlans")), "orchestrators");
    EObject _head = IterableExtensions.<EObject>head(orchestrators);
    boolean _tripleNotEquals = (_head != null);
    if (_tripleNotEquals) {
      int deployedNodeId = this.getFeatureInt(this.getFeatureObject(IterableExtensions.<EObject>head(orchestrators), "deployedOn"), "ID");
      int _lookupConnection = this.lookupConnection(1, deployedNodeId, predictors);
      int _plus = (hops + _lookupConnection);
      hops = _plus;
    }
    final ArrayList<Integer> xtendHighlightingHack = new ArrayList<Integer>();
    final Consumer<EObject> _function = (EObject orchestrator) -> {
      final List<EObject> concreteServices = this.getFeatureList(orchestrator, "concreteServices");
      final Function2<Integer, EObject, Integer> _function_1 = (Integer planHops, EObject concreteService) -> {
        int _lookupConnection_1 = this.lookupConnection(this.getFeatureInt(this.getFeatureObject(orchestrator, "deployedOn"), "ID"), 
          this.getFeatureInt(this.getFeatureObject(concreteService, "providedBy"), "ID"), predictors);
        return Integer.valueOf(((planHops).intValue() + _lookupConnection_1));
      };
      final Integer orchestratorHopsCount = IterableExtensions.<EObject, Integer>fold(concreteServices, Integer.valueOf(0), _function_1);
      xtendHighlightingHack.add(orchestratorHopsCount);
    };
    orchestrators.forEach(_function);
    int _hops = hops;
    int _lookupConnection_1 = this.lookupConnection(1, this.getFeatureInt(this.getFeatureObject(IterableExtensions.<EObject>last(orchestrators), "deployedOn"), "ID"), predictors);
    hops = (_hops + _lookupConnection_1);
    int _hops_1 = hops;
    final Function2<Integer, Integer, Integer> _function_1 = (Integer result, Integer countedHops) -> {
      return Integer.valueOf(((result).intValue() + (countedHops).intValue()));
    };
    Integer _fold = IterableExtensions.<Integer, Integer>fold(xtendHighlightingHack, Integer.valueOf(0), _function_1);
    hops = (_hops_1 + (_fold).intValue());
    return hops;
  }
  
  /**
   * Looks up a node by ID in the predictor model
   */
  public EObject lookupNode(final int id, final EObject predictors) {
    final Function1<EObject, Boolean> _function = (EObject node) -> {
      int _featureInt = this.getFeatureInt(node, "ID");
      return Boolean.valueOf((_featureInt == id));
    };
    return IterableExtensions.<EObject>head(IterableExtensions.<EObject>filter(this.getFeatureList(predictors, "nodes"), _function));
  }
  
  /**
   * Counts the number of hops between two given nodes.
   * Uses the predictor model to query this information.
   */
  public int lookupConnection(final int startNode, final int endNode, final EObject predictors) {
    if ((startNode == endNode)) {
      return 0;
    }
    List<EObject> connections = this.getFeatureList(predictors, "connections");
    final Function1<EObject, Boolean> _function = (EObject connection) -> {
      return Boolean.valueOf((((this.getFeatureInt(this.getFeatureObject(connection, "src"), "ID") == startNode) && (this.getFeatureInt(this.getFeatureObject(connection, "tgt"), "ID") == endNode)) || ((this.getFeatureInt(this.getFeatureObject(connection, "src"), "ID") == endNode) && (this.getFeatureInt(this.getFeatureObject(connection, "tgt"), "ID") == startNode))));
    };
    int hops = this.getFeatureInt(IterableExtensions.<EObject>head(IterableExtensions.<EObject>filter(connections, _function)), "hops");
    return hops;
  }
  
  /**
   * Counts the various types of nodes within a service composition configuration.
   * Returns the number of nodes with of the specific type.
   */
  public int countTypeNodes(final int nodeType, final EObject model, final EObject predictors) {
    List<EObject> orchestrators = this.getFeatureList(IterableExtensions.<EObject>head(this.getFeatureList(model, "concretePlans")), "orchestrators");
    final Function1<EObject, Boolean> _function = (EObject orchestrator) -> {
      int _featureInt = this.getFeatureInt(this.lookupNode(this.getFeatureInt(this.getFeatureObject(orchestrator, "deployedOn"), "ID"), predictors), "type");
      return Boolean.valueOf((_featureInt == nodeType));
    };
    final Function2<Integer, EObject, Integer> _function_1 = (Integer counter, EObject orchestrator) -> {
      int _xblockexpression = (int) 0;
      {
        final List<EObject> concreteServices = this.getFeatureList(orchestrator, "concreteServices");
        final Function1<EObject, Boolean> _function_2 = (EObject concreteService) -> {
          int _featureInt = this.getFeatureInt(this.lookupNode(this.getFeatureInt(this.getFeatureObject(concreteService, "providedBy"), "ID"), predictors), "type");
          return Boolean.valueOf((_featureInt == nodeType));
        };
        int _length = ((Object[])Conversions.unwrapArray(IterableExtensions.<EObject>filter(concreteServices, _function_2), Object.class)).length;
        _xblockexpression = (((counter).intValue() + 1) + _length);
      }
      return Integer.valueOf(_xblockexpression);
    };
    return (int) IterableExtensions.<EObject, Integer>fold(IterableExtensions.<EObject>filter(orchestrators, _function), Integer.valueOf(0), _function_1);
  }
  
  /**
   * Counts the various types of loaded nodes within a service composition configuration.
   * Returns the number of nodes that have the specific load.
   */
  public int countLoadNodes(final int load, final EObject model, final EObject predictors) {
    List<EObject> orchestrators = this.getFeatureList(IterableExtensions.<EObject>head(this.getFeatureList(model, "concretePlans")), "orchestrators");
    final Function1<EObject, Boolean> _function = (EObject orchestrator) -> {
      int _featureInt = this.getFeatureInt(this.lookupNode(this.getFeatureInt(this.getFeatureObject(orchestrator, "deployedOn"), "ID"), predictors), "load");
      return Boolean.valueOf((_featureInt == load));
    };
    final Function2<Integer, EObject, Integer> _function_1 = (Integer counter, EObject orchestrator) -> {
      int _xblockexpression = (int) 0;
      {
        final List<EObject> concreteServices = this.getFeatureList(orchestrator, "concreteServices");
        final Function1<EObject, Boolean> _function_2 = (EObject concreteService) -> {
          int _featureInt = this.getFeatureInt(this.lookupNode(this.getFeatureInt(this.getFeatureObject(concreteService, "providedBy"), "ID"), predictors), "load");
          return Boolean.valueOf((_featureInt == load));
        };
        int _length = ((Object[])Conversions.unwrapArray(IterableExtensions.<EObject>filter(concreteServices, _function_2), Object.class)).length;
        _xblockexpression = (((counter).intValue() + 1) + _length);
      }
      return Integer.valueOf(_xblockexpression);
    };
    return (int) IterableExtensions.<EObject, Integer>fold(IterableExtensions.<EObject>filter(orchestrators, _function), Integer.valueOf(0), _function_1);
  }
  
  /**
   * Helper function getting the value of the named feature (if it exists) for the given EObject
   */
  public Object getFeature(final EObject o, final String feature) {
    Object _xblockexpression = null;
    {
      if ((o == null)) {
        InputOutput.<String>println("Null object given");
      }
      _xblockexpression = o.eGet(o.eClass().getEStructuralFeature(feature));
    }
    return _xblockexpression;
  }
  
  /**
   * Helper function getting the value of the named feature (if it exists) for the given EObject
   * as list of EObjects
   */
  public List<EObject> getFeatureList(final EObject o, final String feature) {
    Object _feature = this.getFeature(o, feature);
    return ((List<EObject>) _feature);
  }
  
  /**
   * Helper function getting the value of the named feature (if it exists) for the given EObject
   * as a single EObject
   */
  public EObject getFeatureObject(final EObject o, final String feature) {
    Object _feature = this.getFeature(o, feature);
    return ((EObject) _feature);
  }
  
  public int getFeatureInt(final EObject o, final String feature) {
    return Integer.parseInt(this.getFeature(o, feature).toString());
  }
}
