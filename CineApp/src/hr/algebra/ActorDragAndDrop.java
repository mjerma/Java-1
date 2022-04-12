/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.dal.Repository;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.ActorTransferable;
import hr.algebra.model.Person;
import hr.algebra.utils.MessageUtils;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

/**
 *
 * @author Mihael
 */
public class ActorDragAndDrop extends javax.swing.JFrame {

    private final Set<Person> movieActors = new TreeSet<>();
    
    private final DefaultListModel<Person> movieActorsModel = new DefaultListModel<>();
    private final DefaultListModel<Person> allActorsModel = new DefaultListModel<>();
    
    private Repository repository;
    private final int movieID;
    private final EditMoviesPanel panel;
    
    /**
     * Creates new form ActorDragAndDrop
     */
    public ActorDragAndDrop(JPanel panel,int movieID) {
        this.panel = (EditMoviesPanel) panel;
        this.movieID = movieID;
        initComponents();
        init();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        lsMovieActors = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnAddActor = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        lsAllActors = new javax.swing.JList<>();
        btnUpdate = new javax.swing.JButton();

        jScrollPane2.setViewportView(lsMovieActors);

        jLabel2.setText("All actors:");

        jLabel3.setText("Movie actors:");

        btnAddActor.setText("Add actor");
        btnAddActor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActorActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(lsAllActors);

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(btnAddActor, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddActor, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActorActionPerformed
        ActorDialog actorDialog = new ActorDialog(this, false);
        actorDialog.setVisible(true);
    }//GEN-LAST:event_btnAddActorActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        try {
            repository.updateMovieActors(movieID, movieActors);
            panel.showMovie();
        } catch (Exception ex) {
            Logger.getLogger(ActorDragAndDrop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddActor;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList<Person> lsAllActors;
    private javax.swing.JList<Person> lsMovieActors;
    // End of variables declaration//GEN-END:variables

    private void init() {
        try {
            initRepository();
            fillMovieActors();
            loadMovieActorsModel();
            loadAllActorsModel();
            initDragNDrop();
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form");
            System.exit(1);
        }
    }
    
    private void initRepository() throws Exception {
        repository = RepositoryFactory.getRepository();
    }
    
    private void loadAllActorsModel() throws Exception {
        allActorsModel.clear();
        repository.selectActors().forEach(actor -> allActorsModel.addElement(actor));
        lsAllActors.setModel(allActorsModel);
    }
    
    private void loadMovieActorsModel() {
        movieActorsModel.clear();
        movieActors.forEach(actor -> movieActorsModel.addElement(actor));
        lsMovieActors.setModel(movieActorsModel);
    }
    
    private void initDragNDrop() {
        lsAllActors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lsAllActors.setDragEnabled(true);
        lsAllActors.setTransferHandler(new ExportTransferHandler());

        lsMovieActors.setDropMode(DropMode.ON);
        lsMovieActors.setTransferHandler(new ImportTransferHandler());
    }

    private void fillMovieActors() throws Exception {
        repository.selectMovie(movieID).get().getActors().forEach(actor -> movieActors.add(actor));
    }

    private class ExportTransferHandler extends TransferHandler {

        @Override
        public int getSourceActions(JComponent c) {
            return COPY;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            return new ActorTransferable(lsAllActors.getSelectedValue());
        }
        
    }

    private class ImportTransferHandler extends TransferHandler {

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(ActorTransferable.ACTOR_FLAVOR);
        }

        @Override
        public boolean importData(TransferSupport support) {
            
            try {
                Person data = (Person) support.getTransferable().getTransferData(ActorTransferable.ACTOR_FLAVOR);
                if (movieActors.add(data)) {
                    loadMovieActorsModel();
                    return true;
                }
                
            } catch (UnsupportedFlavorException | IOException ex) {
                Logger.getLogger(ActorDragAndDrop.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return false;
        }
    }
    
    boolean addActor(Person actor) throws Exception {
        if(repository.createActor(actor)){
            loadAllActorsModel();
            return true;
        }
        return false;
    }
}