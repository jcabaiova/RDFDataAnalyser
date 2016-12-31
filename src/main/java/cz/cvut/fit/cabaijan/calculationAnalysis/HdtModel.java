package cz.cvut.fit.cabaijan.calculationAnalysis;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import cz.cvut.fit.cabaijan.logs.Logger;
import cz.cvut.fit.cabaijan.dbObjects.Dataset;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdtjena.HDTGraph;

/**
 * Created by Janka on 9/26/2016.
 * HdtModel class uses for initiate hdt model which is needed for the calculation of HDT dataset
 */
public class HdtModel {
    /**
     * hdt model, on which calculation will be running
     */
    private Model model;
    /**
     * directory of hdt file
     */
    private String directory;
    /**
     * HDT object
      */
    private HDT hdt;

    /**
     * This is a constructor to initialize HdtModel object.
     */
    public HdtModel() {
        model = null;
        directory = this.getClass().getResource("/importedDatasets/").getFile().toString();
        hdt = null;
    }

    /**
     * This method creates model, if it is needed.
     * @param dataset dataset for which model is created
     * @return object hdt Model
     */
    public Model createModel(Dataset dataset) {
        try {

            hdt = HDTManager.mapHDT(directory + dataset.getFileName(), null);
            HDTGraph graph = new HDTGraph(hdt);
            model = ModelFactory.createModelForGraph(graph);
        } catch (Exception e) {
            Logger.writeException(e);
            return null;
        }
        return model;
    }

    /**
     * this method closes hdt model.
     */
    public void closeModel() {
        try {
            model.close();
            hdt.close();

        } catch (Exception e) {
            Logger.writeException(e);
        }
    }

    /**
     * get model
     * @return model
     */
    public Model getModel() {
        return model;
    }

}
