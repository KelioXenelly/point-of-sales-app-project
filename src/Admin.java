import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ROG
 */
public class Admin extends javax.swing.JFrame {

    /**
     * Creates new form Admin
     */
    public Admin() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuBar3 = new javax.swing.JMenuBar();
        jMenu7 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel13 = new javax.swing.JLabel();
        salesBtn5 = new javax.swing.JButton();
        kelolaBarangBtn = new javax.swing.JButton();
        pemutihanBtn = new javax.swing.JButton();
        kelolaPenggunaBtn = new javax.swing.JButton();
        logoutBtn5 = new javax.swing.JButton();
        ubahHargaBtn = new javax.swing.JButton();
        tabelTransaksiBtn = new javax.swing.JButton();

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        jMenu1.setText("jMenu1");

        jMenu2.setText("File");
        jMenuBar1.add(jMenu2);

        jMenu5.setText("Edit");
        jMenuBar1.add(jMenu5);

        jMenu8.setText("jMenu8");

        jMenu6.setText("jMenu6");

        jMenu7.setText("File");
        jMenuBar3.add(jMenu7);

        jMenu9.setText("Edit");
        jMenuBar3.add(jMenu9);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(102, 153, 255));

        jLabel12.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Selamat datang,");

        jLabel13.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Admin");

        salesBtn5.setText("Point of Sales");
        salesBtn5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesBtn5MouseClicked(evt);
            }
        });

        kelolaBarangBtn.setText("Kelola Barang");
        kelolaBarangBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kelolaBarangBtnMouseClicked(evt);
            }
        });

        pemutihanBtn.setText("Pemutihan Barang");
        pemutihanBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pemutihanBtnMouseClicked(evt);
            }
        });

        kelolaPenggunaBtn.setText("Kelola Pengguna");
        kelolaPenggunaBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kelolaPenggunaBtnMouseClicked(evt);
            }
        });

        logoutBtn5.setText("Logout");
        logoutBtn5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutBtn5MouseClicked(evt);
            }
        });

        ubahHargaBtn.setText("Ubah Harga");
        ubahHargaBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ubahHargaBtnMouseClicked(evt);
            }
        });

        tabelTransaksiBtn.setText("Tabel Transaksi");
        tabelTransaksiBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelTransaksiBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66)
                .addComponent(salesBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kelolaBarangBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pemutihanBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kelolaPenggunaBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ubahHargaBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabelTransaksiBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(logoutBtn5)
                .addGap(34, 34, 34))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salesBtn5)
                            .addComponent(kelolaBarangBtn)
                            .addComponent(pemutihanBtn)
                            .addComponent(kelolaPenggunaBtn)
                            .addComponent(logoutBtn5)
                            .addComponent(ubahHargaBtn)
                            .addComponent(tabelTransaksiBtn))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 613, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void logout() {
        JOptionPane.showMessageDialog(null, "You have been logged out.");

        // Create an instance of LoginPanel
        LoginPanel loginPage = new LoginPanel(); 
        loginPage.setVisible(true); // Show the login panel
        loginPage.setLocationRelativeTo(null);
        
        // Hide the current window (Admin/User Panel)
        this.setVisible(false);
        
        // Optional: Dispose of the current window to free up resources
        this.dispose();
    }
    
    private void salesBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesBtn5MouseClicked
        SalesAdmin PointofSalesPage = new SalesAdmin();
        PointofSalesPage.setVisible(true);
        PointofSalesPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_salesBtn5MouseClicked

    private void logoutBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtn5MouseClicked
        logout();
    }//GEN-LAST:event_logoutBtn5MouseClicked

    private void kelolaBarangBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kelolaBarangBtnMouseClicked
        KelolaBarang kelolaBarangPage = new KelolaBarang();
        kelolaBarangPage.setVisible(true);
        kelolaBarangPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_kelolaBarangBtnMouseClicked

    private void kelolaPenggunaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kelolaPenggunaBtnMouseClicked
        KelolaPengguna kelolaPenggunaPage = new KelolaPengguna();
        kelolaPenggunaPage.setVisible(true);
        kelolaPenggunaPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_kelolaPenggunaBtnMouseClicked

    private void pemutihanBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pemutihanBtnMouseClicked
        PemutihanBarang pemutihanPage = new PemutihanBarang();
        pemutihanPage.setVisible(true);
        pemutihanPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_pemutihanBtnMouseClicked

    private void ubahHargaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ubahHargaBtnMouseClicked
        UbahHarga ubahHargaPage = new UbahHarga();
        ubahHargaPage.setVisible(true);
        ubahHargaPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_ubahHargaBtnMouseClicked

    private void tabelTransaksiBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTransaksiBtnMouseClicked
        TabelTransaksi tabelTransaksiPage = new TabelTransaksi();
        tabelTransaksiPage.setVisible(true);
        tabelTransaksiPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_tabelTransaksiBtnMouseClicked
         
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel12;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JButton kelolaBarangBtn;
    private javax.swing.JButton kelolaPenggunaBtn;
    private javax.swing.JButton logoutBtn5;
    private javax.swing.JButton pemutihanBtn;
    private javax.swing.JButton salesBtn5;
    private javax.swing.JButton tabelTransaksiBtn;
    private javax.swing.JButton ubahHargaBtn;
    // End of variables declaration//GEN-END:variables
}
