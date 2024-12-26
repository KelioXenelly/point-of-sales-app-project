import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class KelolaPengguna extends javax.swing.JFrame {
    private DefaultTableModel model = null;
    private PreparedStatement stm;
    private ResultSet rs;
    String query = "SELECT users.user_id, users.username, users.password, roles.roles_name " +
                   "FROM users " +
                   "INNER JOIN roles ON users.roles_id = roles.roles_id";
    koneksi conn = new koneksi();
    
    public KelolaPengguna() {
        initComponents();
        conn.connect();
        if(conn.getConnection() == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        refreshTable();
    }
    
    class user extends KelolaPengguna {
        int user_id, roles_id;
        String username, password;
        
        public user() {
            username = usernameTxt.getText();
            password = passwordTxt.getText();
            
            if(roles_nameTxt.getSelectedItem().equals("Admin")) {
                roles_id = 1;
            } else {
                roles_id = 2;
            }
        }
    }
    
    public void refreshTable() {
        model = new DefaultTableModel();
        model.addColumn("ID User");
        model.addColumn("Username");
        model.addColumn("Password");
        model.addColumn("Hak Akses");
        tablePengguna.setModel(model);
        try {
            this.stm = conn.getConnection().prepareStatement(query);
            this.rs = this.stm.executeQuery();
            while(rs.next()) {
                Object[]  data =  {
                    rs.getString("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("roles_name")
                };
                model.addRow(data);
            }
            
            // Close resources
            rs.close();
            stm.close();
            System.out.println("Connection Closed.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        id_userTxt.setText("");
        usernameTxt.setText("");
        passwordTxt.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel13 = new javax.swing.JLabel();
        salesBtn5 = new javax.swing.JButton();
        kelolaBarangBtn = new javax.swing.JButton();
        transaksiBtn = new javax.swing.JButton();
        kelolaPenggunaBtn = new javax.swing.JButton();
        logoutBtn5 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePengguna = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        id_userTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        usernameTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        passwordTxt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        roles_nameTxt = new javax.swing.JComboBox<>();
        tambahBtn = new javax.swing.JButton();
        ubahBtn = new javax.swing.JButton();
        hapusBtn = new javax.swing.JButton();

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

        transaksiBtn.setText("Tabel Transaksi");
        transaksiBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                transaksiBtnMouseClicked(evt);
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
                .addComponent(transaksiBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kelolaPenggunaBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                            .addComponent(transaksiBtn)
                            .addComponent(kelolaPenggunaBtn)
                            .addComponent(logoutBtn5))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tablePengguna.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID User", "Username", "Password", "Hak Akses"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePengguna.getTableHeader().setReorderingAllowed(false);
        tablePengguna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePenggunaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePengguna);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel3.setText("Tabel Pengguna");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("ID User:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel2.setText("Akun Pengguna");

        id_userTxt.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Username:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Password");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Hak Akses:");

        roles_nameTxt.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Admin", "User" }));
        roles_nameTxt.setSelectedIndex(0);

        tambahBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tambahBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Plus.png"))); // NOI18N
        tambahBtn.setText("Tambah");
        tambahBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahBtnActionPerformed(evt);
            }
        });

        ubahBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ubahBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Edit.png"))); // NOI18N
        ubahBtn.setText("Ubah");
        ubahBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ubahBtnActionPerformed(evt);
            }
        });

        hapusBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        hapusBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Trash.png"))); // NOI18N
        hapusBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(tambahBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ubahBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hapusBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(id_userTxt)
                    .addComponent(usernameTxt)
                    .addComponent(passwordTxt)
                    .addComponent(roles_nameTxt, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(id_userTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usernameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(roles_nameTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tambahBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hapusBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ubahBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
        // Create an instance of Sales
        SalesAdmin PointofSalesPage = new SalesAdmin();
        PointofSalesPage.setVisible(true); // Show the login panel
        PointofSalesPage.setLocationRelativeTo(null);

        // Hide the current window
        this.setVisible(false);
    }//GEN-LAST:event_salesBtn5MouseClicked

    private void kelolaBarangBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kelolaBarangBtnMouseClicked
        // Create an instance of Sales
        KelolaBarang kelolaBarangPage = new KelolaBarang();
        kelolaBarangPage.setVisible(true); // Show the kelolaBarangPage
        kelolaBarangPage.setLocationRelativeTo(null);

        // Hide the current window
        this.setVisible(false);
    }//GEN-LAST:event_kelolaBarangBtnMouseClicked

    private void transaksiBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transaksiBtnMouseClicked
        // Create an instance of Sales
        Transaksi transaksiPage = new Transaksi();
        transaksiPage.setVisible(true);
        transaksiPage.setLocationRelativeTo(null);

        // Hide the current window
        this.setVisible(false);
    }//GEN-LAST:event_transaksiBtnMouseClicked

    private void kelolaPenggunaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kelolaPenggunaBtnMouseClicked
        // Create an instance of Sales
        KelolaPengguna kelolaPenggunaPage = new KelolaPengguna();
        kelolaPenggunaPage.setVisible(true);
        kelolaPenggunaPage.setLocationRelativeTo(null);

        // Hide the current window
        this.setVisible(false);
    }//GEN-LAST:event_kelolaPenggunaBtnMouseClicked

    private void logoutBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtn5MouseClicked
        logout();
    }//GEN-LAST:event_logoutBtn5MouseClicked

    private void ubahBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ubahBtnActionPerformed
        try {
            user usr = new user();
            if(!usr.username.isEmpty() && !usr.password.isEmpty()) {
                this.stm = conn.getConnection().prepareStatement("update users set username=?,  password=?, roles_id=? where user_id=?" );
                stm.setString(1, usr.username);
                stm.setString(2, usr.password);
                stm.setInt(3, usr.roles_id);

                int idUserDb = Integer.parseInt(model.getValueAt(tablePengguna.getSelectedRow(), 0).toString());
                usr.user_id = idUserDb;
                stm.setInt(4, usr.user_id);

                int rowsAffected = stm.executeUpdate(); // Capture affected rows

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Data Berhasil Terubah");
                } else {
                    JOptionPane.showMessageDialog(null, "Data Tidak Ditemukan");
                }

                stm.executeUpdate();

                refreshTable();
            } else {
                    JOptionPane.showMessageDialog(null, "Belum ada data pengguna yang dipilih", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_ubahBtnActionPerformed

    private void hapusBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusBtnActionPerformed
        try {
            user usr = new user();
            if(!usr.username.isEmpty() && !usr.password.isEmpty()) {
                this.stm = conn.getConnection().prepareStatement("delete from users where user_id=?");

                int idUserDb = Integer.parseInt(model.getValueAt(tablePengguna.getSelectedRow(), 0).toString());
                usr.user_id = idUserDb;
                stm.setInt(1, usr.user_id);

                stm.executeUpdate();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(null, "Belum ada data pengguna yang dipilih", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_hapusBtnActionPerformed

    private void tambahBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahBtnActionPerformed
        try {
            user usr = new user();
            if(!usr.username.isEmpty() && !usr.password.isEmpty()) {
                this.stm = conn.getConnection().prepareStatement("insert into users values(?, ?, ?, ?)");
                
                stm.setInt(1, 0);
                stm.setString(2, usr.username);
                stm.setString(3, usr.password);
                stm.setInt(4, usr.roles_id);

                stm.executeUpdate();
                refreshTable();
                
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
            } else {
                JOptionPane.showMessageDialog(null, "Harap memasukkan Username dan Password", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_tambahBtnActionPerformed

    private void tablePenggunaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePenggunaMouseClicked
        id_userTxt.setText(model.getValueAt(tablePengguna.getSelectedRow(), 0).toString());
        usernameTxt.setText(model.getValueAt(tablePengguna.getSelectedRow(), 1).toString());
        passwordTxt.setText(model.getValueAt(tablePengguna.getSelectedRow(), 2).toString());
        
        if(model.getValueAt(tablePengguna.getSelectedRow(), 3).toString().equals("admin")) {
            roles_nameTxt.setSelectedIndex(0);
        } else {
            roles_nameTxt.setSelectedIndex(1);
        }
    }//GEN-LAST:event_tablePenggunaMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KelolaPengguna().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton hapusBtn;
    private javax.swing.JTextField id_userTxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton kelolaBarangBtn;
    private javax.swing.JButton kelolaPenggunaBtn;
    private javax.swing.JButton logoutBtn5;
    private javax.swing.JTextField passwordTxt;
    private javax.swing.JComboBox<String> roles_nameTxt;
    private javax.swing.JButton salesBtn5;
    private javax.swing.JTable tablePengguna;
    private javax.swing.JButton tambahBtn;
    private javax.swing.JButton transaksiBtn;
    private javax.swing.JButton ubahBtn;
    private javax.swing.JTextField usernameTxt;
    // End of variables declaration//GEN-END:variables
}
