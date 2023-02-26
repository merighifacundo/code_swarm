/* This file is part of code_swarm.

code_swarm is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

code_swarm is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with code_swarm.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import org.codeswarm.repository.svn.SVNHistory;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.prefs.Preferences;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This is the main entry point for the application.<br />
 * This version is a fast implementation of a user-interface.<br />
 * Further versions should be improved and include stuff like i18n.<br />
 * The usage of Swing-Application-Framework and Beans-Binding could help in the
 * future.<br />
 *
 * NOTE: This dialog was made using Netbeans. Modifications should be done with
 * Netbeans (at least 6.0) to reflect them to the corresponding netbeans-form.
 * @author tpraxl
 */
public class MainView extends javax.swing.JFrame {
    // This class couples SVNHistory too tightly.
    // TODO The concrete History-Implementation should be created by a Factory
    // depending on the URL and/or other settings.

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
     * initializes the Dialog and sets the dialog-values according to the
     * user's preferences (the last values entered).
     * @param args java arguments passed to the main method. The first parameter
     * will be passed to {@link code_swarm}. It specifies the config-file.
     */
    public MainView(String[] args) {
        this.args = args;
        initComponents();
        this.getRootPane().setDefaultButton(goButton);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        goButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);


        goButton.setText("GO");
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });


        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 242, Short.MAX_VALUE)
                        .add(goButton)))
                .addContainerGap()))));
        
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(goButton))));
                
        

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * gets called when the user presses the "Go"-Button.<br />
     * It manages fetching the repository entries and serving it to
     * {@link code_swarm}. It starts code_swarm after fetching the repository
     * entries.
     * @param evt The ActionEvent from Swing
     */
private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
    Runnable run = new Runnable() {
        public void run() {
            goButton.setEnabled(false);
            
            try {
                System.out.println(args[0]);
                CodeSwarmConfig cfg = new CodeSwarmConfig(args[0]);
                cfg.setInputFile("data/sample.events.json"); // "data/sample-repevents.xml"
                CodeSwarm.start(cfg);
                dispose();
            } catch (IOException e) {
                System.err.println("Failed due to exception: " + e.getMessage());
                goButton.setEnabled(true);
            
            }
        }
    };
    new Thread(run).start();
}//GEN-LAST:event_goButtonActionPerformed

/**
 * gets called when the user presses the "Clear Cache" Button. It clears the
 * version cache.
 * @param evt the Swing ActionEvent.
 */
private void clearCacheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearCacheActionPerformed
    SVNHistory.clearCache();
}//GEN-LAST:event_clearCacheActionPerformed

    /**
     * This is the main entry-point. It sets the native Look&Feel, creates and
     * shows the MainView.
    * @param args the command line arguments. The first parameter
     * will be passed to {@link code_swarm}. It specifies the config-file.
    */
    public static void main(final String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Toolkit.getDefaultToolkit().setDynamicLayout(true);
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }catch(ClassNotFoundException e){
                    // not that fatal. No need to log.
                }catch(InstantiationException e){
                    // not that fatal. No need to log.
                }catch(IllegalAccessException e){
                    // not that fatal. No need to log.
                }catch(UnsupportedLookAndFeelException e){
                    // not that fatal. No need to log.
                }
                try {
                    File f = new File("data/log.properties");
                    InputStream in = new FileInputStream(f);
                    LogManager.getLogManager().readConfiguration(in);
                    in.close();
                } catch (IOException ex) {
                    // no problem. Standard-logging is performed (Console)
                    Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                }
                new MainView(args).setVisible(true);
            }
        });
    }
    private String[] args;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    
    private javax.swing.JButton goButton;
    // End of variables declaration//GEN-END:variables

}
