
import java.awt.Color;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ROG
 */
public class PemutihanBarangUser extends javax.swing.JFrame {
    protected DefaultTableModel model = null;
    protected PreparedStatement stg;
    protected PreparedStatement stm;
    protected PreparedStatement stt;
    protected PreparedStatement sta;
    protected PreparedStatement sth;
    protected ResultSet rs;
    protected ResultSet rt;
    String query = "SELECT barang.barang_id, kode_barang, nama_barang, detailbarang.qty, "
                 + "detailbarang.harga_beli, detailbarang.harga_jual, detailbarang.profit "
                 + "FROM barang "
                 + "LEFT JOIN kodebarang ON barang.kode_barang_id = kodebarang.kode_barang_id "
                 + "LEFT JOIN detailbarang ON barang.barang_id = detailbarang.barang_id "
                 + "WHERE detailbarang.qty > 0 "
                 + "ORDER BY kode_barang ASC";
    koneksi conn = new koneksi();
    
    /**
     * Creates new form PemutihanBarang
     */
    public PemutihanBarangUser() {
        initComponents();
        conn.connect();
        if(conn.getConnection() == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            this.stm = conn.getConnection().prepareStatement("SELECT kode_barang FROM kodebarang");
            this.rs = this.stm.executeQuery();
            kodeBarangComboBox.addItem("Pilih sebuah barang");
            while(rs.next()) {
                kodeBarangComboBox.addItem(rs.getString("kode_barang"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaBarang.class.getName()).log(Level.SEVERE, null, ex);
        }
        refreshTable();
    }
    
    class putihBarang extends PemutihanBarang {
        String kode_barang, nama_barang, keterangan;
        Double harga_beli, harga_jual;
        Date tanggal_pemutihan;
        int qty, pemutihan_id, barang_id, detail_barang_id;
        boolean confirm;
        
        public putihBarang() {
            try {
                kode_barang = kodeBarangComboBox.getSelectedItem().toString();
                tanggal_pemutihan = (Date) tanggalUbahChooser.getDate();
                nama_barang = namaBarangTxt.getText();
                qty = Integer.parseInt(qtyTxt.getText());
                harga_beli = Double.valueOf(hargaBeliTxt.getText());
                keterangan = keteranganTextBox.getText();
                confirm = confirmCheckBox.isSelected();
            } catch(Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
    
    public void refreshTable() {
        model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Kode");
        model.addColumn("Nama Barang");
        model.addColumn("Qty");
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Profit");
        tableBarang.setModel(model);
        
        try {
            this.stm = conn.getConnection().prepareStatement(query);
            this.rs = this.stm.executeQuery();
            int i = 1;
            while(rs.next()) {
                Object[]  data =  {
                    i,
                    rs.getString("kode_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("qty"),
                    rs.getString("harga_beli"),
                    rs.getString("harga_jual"),
                    rs.getString("profit")
                };
                model.addRow(data);
                i++;
            }
            
            // Close resources
            rs.close();
            stm.close();
            System.out.println("Connection Closed.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        namaBarangTxt.setText("Masukkan Nama Barang");
        kodeBarangComboBox.setSelectedItem("Pilih sebuah barang");
        tanggalUbahChooser.setDate(null);
        namaBarangTxt.setText("");
        qtyTxt.setText("");
        hargaBeliTxt.setText("");
        keteranganTextBox.setText("Tulis Keterangan");
    }
    
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
    
    public void addPlaceHolderStyle(JTextField textField) {
        Font font = textField.getFont();
        font = font.deriveFont(Font.ITALIC);
        textField.setFont(font);
        textField.setForeground(Color.gray);
    }
    
    public void removePlaceHolderStyle(JTextField textField) {
        Font font = textField.getFont();
        font = font.deriveFont(Font.PLAIN|Font.BOLD);
        textField.setFont(font);
        textField.setForeground(Color.black);
    }
    
    public void addPlaceHolderStyle(JTextArea TextArea) {
        Font font = TextArea.getFont();
        font = font.deriveFont(Font.ITALIC);
        TextArea.setFont(font);
        TextArea.setForeground(Color.gray);
    }
    
    public void removePlaceHolderStyle(JTextArea TextArea) {
        Font font = TextArea.getFont();
        font = font.deriveFont(Font.PLAIN|Font.BOLD);
        TextArea.setFont(font);
        TextArea.setForeground(Color.black);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableBarang = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        transaksiPemutihanTxt = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        namaBarangTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        qtyTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        kodeBarangComboBox = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        keteranganTextBox = new javax.swing.JTextArea();
        tanggalUbahChooser = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        prosesPemutihanBtn = new javax.swing.JButton();
        confirmCheckBox = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        hargaBeliTxt = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel18 = new javax.swing.JLabel();
        salesBtn5 = new javax.swing.JButton();
        kelolaBarangBtn = new javax.swing.JButton();
        pemutihanBtn = new javax.swing.JButton();
        logoutBtn5 = new javax.swing.JButton();
        ubahHargaBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Tabel Barang");

        tableBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Kode", "Nama Barang", "Qty", "Harga Beli", "Harga Jual", "Profit", "ID Pembelian"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableBarang.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tableBarang.setShowGrid(true);
        tableBarang.getTableHeader().setReorderingAllowed(false);
        tableBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableBarangMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tableBarang);
        if (tableBarang.getColumnModel().getColumnCount() > 0) {
            tableBarang.getColumnModel().getColumn(7).setMinWidth(0);
            tableBarang.getColumnModel().getColumn(7).setPreferredWidth(0);
            tableBarang.getColumnModel().getColumn(7).setMaxWidth(0);
        }

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 614, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        transaksiPemutihanTxt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        transaksiPemutihanTxt.setText("Transaksi Pemutihan Barang");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Kode Barang:");

        namaBarangTxt.setToolTipText("");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Nama Barang:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Qty:");

        qtyTxt.setToolTipText("");
        qtyTxt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        qtyTxt.setOpaque(false);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Keterangan:");

        kodeBarangComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                kodeBarangComboBoxItemStateChanged(evt);
            }
        });

        keteranganTextBox.setColumns(20);
        keteranganTextBox.setRows(5);
        keteranganTextBox.setText("Tulis Keterangan");
        keteranganTextBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                keteranganTextBoxFocusGained(evt);
            }
        });
        jScrollPane4.setViewportView(keteranganTextBox);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Tanggal Pemutihan:");

        prosesPemutihanBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        prosesPemutihanBtn.setText("Proses");
        prosesPemutihanBtn.setAlignmentY(0.0F);
        prosesPemutihanBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prosesPemutihanBtnMouseClicked(evt);
            }
        });

        confirmCheckBox.setBackground(new java.awt.Color(255, 255, 255));
        confirmCheckBox.setText("Saya yakin ingin memutihkan barang terkait");
        confirmCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmCheckBoxActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Harga Beli:");

        hargaBeliTxt.setEditable(false);
        hargaBeliTxt.setToolTipText("");
        hargaBeliTxt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        hargaBeliTxt.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(namaBarangTxt)
                    .addComponent(qtyTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4)
                    .addComponent(hargaBeliTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(confirmCheckBox)
                            .addComponent(jLabel7)
                            .addComponent(transaksiPemutihanTxt)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(kodeBarangComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel16)
                                    .addComponent(tanggalUbahChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel8)
                            .addComponent(jLabel15)
                            .addComponent(jLabel9))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(prosesPemutihanBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transaksiPemutihanTxt)
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tanggalUbahChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(kodeBarangComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaBarangTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(qtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(hargaBeliTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(confirmCheckBox)
                .addGap(27, 27, 27)
                .addComponent(prosesPemutihanBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(102, 153, 255));

        jLabel17.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Selamat datang,");

        jLabel18.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("User");

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66)
                .addComponent(salesBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kelolaBarangBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pemutihanBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ubahHargaBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                .addComponent(logoutBtn5)
                .addGap(34, 34, 34))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salesBtn5)
                            .addComponent(kelolaBarangBtn)
                            .addComponent(pemutihanBtn)
                            .addComponent(logoutBtn5)
                            .addComponent(ubahHargaBtn))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void kodeBarangComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_kodeBarangComboBoxItemStateChanged
        try {
            String kodeBarang = kodeBarangComboBox.getSelectedItem().toString();
            if(!kodeBarang.equals("Pilih sebuah barang")) {
                String queryGetBarang = "SELECT kodebarang.kode_barang, barang.nama_barang, detailbarang.harga_jual "
                + "FROM barang "
                + "INNER JOIN kodebarang ON kodebarang.kode_barang_id = barang.kode_barang_id "
                + "INNER JOIN detailbarang ON detailbarang.barang_id = barang.barang_id "
                + "WHERE kodebarang.kode_barang = ?";

                this.stm = conn.getConnection().prepareStatement(queryGetBarang);
                this.stm.setString(1, kodeBarang);
                this.rs = stm.executeQuery();

                if (rs.next()) {
                    namaBarangTxt.setText(rs.getString("nama_barang"));
                    qtyTxt.setText(String.valueOf(rs.getDouble("harga_jual")));
                } else {
                    namaBarangTxt.setText("");
                }

                stm.close();
                rs.close();
            } else {
                namaBarangTxt.setText("");

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_kodeBarangComboBoxItemStateChanged

    private void keteranganTextBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_keteranganTextBoxFocusGained
        if(keteranganTextBox.getText().equals("Tulis Keterangan")) {
            keteranganTextBox.setText(null);
            keteranganTextBox.requestFocus();
            // Remove placeholder style
            removePlaceHolderStyle(keteranganTextBox);
        }
    }//GEN-LAST:event_keteranganTextBoxFocusGained

    private void prosesPemutihanBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prosesPemutihanBtnMouseClicked
        try {
            putihBarang pth = new putihBarang();
            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow != -1) {
                Object qty = model.getValueAt(selectedRow, 3);
                Object hargaBeli = model.getValueAt(selectedRow, 4);
                Object hargaJual = model.getValueAt(selectedRow, 5);
                
                int qtyTable = Integer.parseInt(qty.toString());
                Double hargaBeliTable = Double.valueOf(hargaBeli.toString());
                Double hargaJualTable = Double.valueOf(hargaJual.toString());
                
                int qtyNow = qtyTable - pth.qty;
                Double profitNow = (qtyNow * hargaJualTable) - (qtyNow * hargaBeliTable);
                
                if(pth.nama_barang == null || pth.nama_barang.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Barang belum dipilih!");
                    return;
                }
                if(pth.qty <= 0) {
                    JOptionPane.showMessageDialog(null, "Qty tidak valid!");
                    return;
                }
                if(pth.confirm == false) {
                    JOptionPane.showMessageDialog(null, "Anda belum menyatakan bahwa anda ingin memutihkan barang terkait.");
                    return;
                }
                
                if(!pth.nama_barang.isEmpty() && pth.tanggal_pemutihan != null &&
                    pth.harga_beli != 0) {
                    
                    this.stm = conn.getConnection().prepareStatement("SELECT barang_id, nama_barang FROM barang WHERE nama_barang = ?");
                    this.stm.setString(1, pth.nama_barang);
                    this.rs = this.stm.executeQuery();

                    if(rs.next()) {
                        pth.barang_id = this.rs.getInt("barang_id");
                    } else {
                        JOptionPane.showMessageDialog(null, "Barang ID tidak ditemukan untuk barang tersebut!");
                        pth.barang_id = -1;
                    }
                    stm.close();
                    rs.close();
                    
                    // Get detail_barang_id
                    this.stm = conn.getConnection().prepareStatement("SELECT detail_barang_id, qty, harga_beli, harga_jual FROM detailbarang "
                                                                   + "WHERE barang_id = ?");
                    this.stm.setInt(1, pth.barang_id);
                    this.rs = this.stm.executeQuery();
                    
                    while(rs.next()) {
                        if(qtyTable == rs.getInt("qty") && hargaBeliTable == rs.getDouble("harga_beli")) {
                            pth.detail_barang_id = this.rs.getInt("detail_barang_id");
                            pth.harga_jual = this.rs.getDouble("harga_jual");
                            break;
                        }
                    }
                    stm.close();
                    rs.close();
                    
                    String queryUpdateBarang = "UPDATE detailbarang SET qty = ?, profit = ? WHERE detail_barang_id = ?";
                    this.stm = conn.getConnection().prepareStatement(queryUpdateBarang);
                    this.stm.setInt(1, qtyNow);
                    this.stm.setDouble(2, profitNow);
                    this.stm.setInt(3, pth.detail_barang_id);
                    this.stm.executeUpdate();
                    stm.close();
                    rs.close();
                    
                    String queryPemutihan = "INSERT INTO pemutihan "
                                          + "(tanggal_pemutihan, barang_id, qty, harga_beli, harga_jual, keterangan, confirm) "
                                          + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    this.stm = conn.getConnection().prepareStatement(queryPemutihan);
                    java.sql.Date sqlDate = new java.sql.Date(pth.tanggal_pemutihan.getTime());
                    this.stm.setDate(1, sqlDate);
                    this.stm.setInt(2, pth.barang_id);
                    this.stm.setInt(3, pth.qty);
                    this.stm.setDouble(4, pth.harga_beli);
                    this.stm.setDouble(5, pth.harga_jual);
                    this.stm.setString(6, pth.keterangan);
                    this.stm.setBoolean(7, pth.confirm);
                    this.stm.executeUpdate();
                    stm.close();
                    rs.close();
                    
                    JOptionPane.showMessageDialog(null, "Transaksi Pemutihan Barang Berhasil!");

                    Thread.sleep(1000);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Harap memasukkan data secara lengkap", "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_prosesPemutihanBtnMouseClicked

    private void tableBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBarangMouseClicked
        try {
            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow != -1) {
                Object kodeBarang = model.getValueAt(selectedRow, 1);
                Object namaBarang = model.getValueAt(selectedRow, 2);
                Object qty = model.getValueAt(selectedRow, 3);
                Object hargaBeli = model.getValueAt(selectedRow, 4);

                namaBarangTxt.setText(namaBarang != null ? namaBarang.toString() : "");
                kodeBarangComboBox.setSelectedItem(kodeBarang != null ? kodeBarang.toString() : "");
                qtyTxt.setText(qty != null ? qty.toString() : "");
                hargaBeliTxt.setText(hargaBeli != null ? hargaBeli.toString() : "");
            } else {
                JOptionPane.showMessageDialog(null, "No row selected.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_tableBarangMouseClicked

    private void confirmCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmCheckBoxActionPerformed

    private void salesBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesBtn5MouseClicked
        SalesUser PointofSalesPage = new SalesUser();
        PointofSalesPage.setVisible(true);
        PointofSalesPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_salesBtn5MouseClicked

    private void kelolaBarangBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kelolaBarangBtnMouseClicked
        KelolaBarangUser kelolaBarangPage = new KelolaBarangUser();
        kelolaBarangPage.setVisible(true);
        kelolaBarangPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_kelolaBarangBtnMouseClicked

    private void pemutihanBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pemutihanBtnMouseClicked
        PemutihanBarangUser pemutihanPage = new PemutihanBarangUser();
        pemutihanPage.setVisible(true);
        pemutihanPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_pemutihanBtnMouseClicked

    private void logoutBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtn5MouseClicked
        logout();
    }//GEN-LAST:event_logoutBtn5MouseClicked

    private void ubahHargaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ubahHargaBtnMouseClicked
        UbahHargaUser ubahHargaPage = new UbahHargaUser();
        ubahHargaPage.setVisible(true);
        ubahHargaPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_ubahHargaBtnMouseClicked

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
            java.util.logging.Logger.getLogger(PemutihanBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PemutihanBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PemutihanBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PemutihanBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PemutihanBarang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox confirmCheckBox;
    private javax.swing.JTextField hargaBeliTxt;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JButton kelolaBarangBtn;
    private javax.swing.JTextArea keteranganTextBox;
    private javax.swing.JComboBox<String> kodeBarangComboBox;
    private javax.swing.JButton logoutBtn5;
    private javax.swing.JTextField namaBarangTxt;
    private javax.swing.JButton pemutihanBtn;
    private javax.swing.JButton prosesPemutihanBtn;
    private javax.swing.JTextField qtyTxt;
    private javax.swing.JButton salesBtn5;
    private javax.swing.JTable tableBarang;
    private com.toedter.calendar.JDateChooser tanggalUbahChooser;
    private javax.swing.JLabel transaksiPemutihanTxt;
    private javax.swing.JButton ubahHargaBtn;
    // End of variables declaration//GEN-END:variables
}
