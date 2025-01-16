
import java.awt.Color;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ROG
 */
public class UbahHargaUser extends javax.swing.JFrame {
    protected DefaultTableModel model = null;
    protected PreparedStatement stg;
    protected PreparedStatement stm;
    protected PreparedStatement stt;
    protected PreparedStatement sta;
    protected PreparedStatement sth;
    protected ResultSet rs;
    protected ResultSet rt;
    String query = "SELECT barang.barang_id, kode_barang, barang.nama_barang, detailbarang.qty, "
                 + "detailbarang.harga_beli, detailbarang.harga_jual, detailbarang.profit, "
                 + "revisihargajual.tanggal_revisi, revisihargajual.harga_jual_baru, revisihargajual.keterangan "
                 + "FROM barang "
                 + "LEFT JOIN kodebarang ON barang.kode_barang_id = kodebarang.kode_barang_id "
                 + "LEFT JOIN detailbarang ON barang.barang_id = detailbarang.barang_id "
                 + "LEFT JOIN revisihargajual ON barang.barang_id = revisihargajual.barang_id "
                 + "WHERE qty > 0";
    koneksi conn = new koneksi();
    
    /**
     * Creates new form UbahHarga
     */
    public UbahHargaUser() {
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
            refreshTable();
        } catch (SQLException ex) {
            Logger.getLogger(KelolaBarang.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    class ubahHargaJual extends UbahHarga {
        String kode_barang, nama_barang, keterangan;
        Double harga_lama, harga_baru;
        Date tanggal_ubah;
        int barang_id, revisihargajual_id;
        
        public ubahHargaJual() {
            try {
                kode_barang = kodeBarangComboBox.getSelectedItem().toString();
                tanggal_ubah = (Date) tanggalUbahChooser.getDate();
                nama_barang = namaBarangTxt.getText();
                harga_lama = Double.valueOf(hargaLamaTxt.getText());
                harga_baru = Double.valueOf(hargaBaruTxt.getText());
                keterangan = keteranganTextBox.getText();
            } catch(Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            
        }
    }
    
    public void refreshTable() {
        model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Tanggal Revisi");
        model.addColumn("Kode Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Harga Jual Lama");
        model.addColumn("Harga Jual Baru");
        model.addColumn("Keterangan");
        UbahHargaTable.setModel(model);
        
        try {
            this.stm = conn.getConnection().prepareStatement("SELECT kodebarang.kode_barang, nama_barang, tanggal_revisi, harga_jual_lama, "
                    + "harga_jual_baru, revisihargajual.keterangan "
                    + "FROM revisihargajual "
                    + "LEFT JOIN barang ON barang.barang_id = revisihargajual.barang_id "
                    + "LEFT JOIN detailbarang ON barang.barang_id = detailbarang.barang_id "
                    + "LEFT JOIN kodebarang ON barang.kode_barang_id = kodebarang.kode_barang_id "
                    + "GROUP BY barang.nama_barang "
                    + "ORDER BY tanggal_revisi ASC");
            this.rs = this.stm.executeQuery();
            int i = 1;
            while(rs.next()) {
                Object[]  data =  {
                    i,
                    rs.getString("tanggal_revisi"),
                    rs.getString("kode_barang"),
                    rs.getString("nama_barang"), 
                    rs.getString("harga_jual_lama"),
                    rs.getString("harga_jual_baru"),
                    rs.getString("keterangan"),
                };
                model.addRow(data);
                i++;
            }
            
            // Close resources
            rs.close();
            stm.close();
            System.out.println("Connection Closed.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat memuat data: " + e.getMessage());
        }
        kodeBarangComboBox.setSelectedItem("Pilih sebuah barang");
        tanggalUbahChooser.setDate(null);
        namaBarangTxt.setText("");
        hargaLamaTxt.setText("");
        hargaBaruTxt.setText("");
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
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        UbahHargaTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        transaksiUbahHargaTxt = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        namaBarangTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        hargaLamaTxt = new javax.swing.JTextField();
        hargaBaruTxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        kodeBarangComboBox = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        keteranganTextBox = new javax.swing.JTextArea();
        tanggalUbahChooser = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        prosesUbahBarangBtn = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel19 = new javax.swing.JLabel();
        salesBtn5 = new javax.swing.JButton();
        kelolaBarangBtn = new javax.swing.JButton();
        pemutihanBtn = new javax.swing.JButton();
        logoutBtn5 = new javax.swing.JButton();
        ubahHargaBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Tabel Riwayat Ubah Harga");

        UbahHargaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal Revisi", "Kode Barang", "Nama Barang", "Harga Jual Lama", "Harga Jual Baru", "Keterangan"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        UbahHargaTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        UbahHargaTable.setShowGrid(true);
        UbahHargaTable.getTableHeader().setReorderingAllowed(false);
        UbahHargaTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UbahHargaTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(UbahHargaTable);
        if (UbahHargaTable.getColumnModel().getColumnCount() > 0) {
            UbahHargaTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            UbahHargaTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            UbahHargaTable.getColumnModel().getColumn(2).setPreferredWidth(60);
            UbahHargaTable.getColumnModel().getColumn(3).setPreferredWidth(140);
            UbahHargaTable.getColumnModel().getColumn(4).setPreferredWidth(80);
            UbahHargaTable.getColumnModel().getColumn(5).setPreferredWidth(80);
            UbahHargaTable.getColumnModel().getColumn(6).setPreferredWidth(180);
        }

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        transaksiUbahHargaTxt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        transaksiUbahHargaTxt.setText("Transaksi Ubah Harga Jual");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Kode Barang:");

        namaBarangTxt.setToolTipText("");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Nama Barang:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Harga Lama");

        hargaLamaTxt.setEditable(false);
        hargaLamaTxt.setToolTipText("");
        hargaLamaTxt.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        hargaLamaTxt.setEnabled(false);

        hargaBaruTxt.setText("Input Harga Jual Baru");
        hargaBaruTxt.setToolTipText("");
        hargaBaruTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                hargaBaruTxtFocusGained(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Harga Baru");

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
        jScrollPane2.setViewportView(keteranganTextBox);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Tanggal Ubah:");

        prosesUbahBarangBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        prosesUbahBarangBtn.setText("Proses");
        prosesUbahBarangBtn.setAlignmentY(0.0F);
        prosesUbahBarangBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prosesUbahBarangBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(namaBarangTxt)
                    .addComponent(hargaLamaTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(hargaBaruTxt)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(transaksiUbahHargaTxt)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(kodeBarangComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel16)
                                    .addComponent(tanggalUbahChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel8)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(prosesUbahBarangBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transaksiUbahHargaTxt)
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
                .addComponent(hargaLamaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hargaBaruTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(prosesUbahBarangBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(102, 153, 255));

        jLabel17.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Selamat datang,");

        jLabel19.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("User");

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

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void keteranganTextBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_keteranganTextBoxFocusGained
        if(keteranganTextBox.getText().equals("Tulis Keterangan")) {
            keteranganTextBox.setText(null);
            keteranganTextBox.requestFocus();
            // Remove placeholder style
            removePlaceHolderStyle(keteranganTextBox);
        }
    }//GEN-LAST:event_keteranganTextBoxFocusGained

    private void prosesUbahBarangBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prosesUbahBarangBtnMouseClicked
        try {
            ubahHargaJual ubh = new ubahHargaJual();
            if(!ubh.nama_barang.isEmpty() && ubh.tanggal_ubah != null && 
                ubh.harga_lama != 0 && ubh.harga_baru != 0) {
                
                this.stt = conn.getConnection().prepareStatement("SELECT barang_id, nama_barang FROM barang WHERE nama_barang = ?");
                this.stt.setString(1, ubh.nama_barang);
                this.rs = this.stt.executeQuery();
                
                if(rs.next()) {
                    ubh.barang_id = this.rs.getInt("barang_id");
                } else {
                    JOptionPane.showMessageDialog(null, "Barang ID tidak ditemukan untuk barang tersebut!");
                    ubh.barang_id = -1;
                }
                stt.close();
                rs.close();

                // Validasi apakah barang_id tersedia
                String cekBarang = "SELECT COUNT(*) FROM barang WHERE barang_id = ?";
                PreparedStatement cekStm = conn.getConnection().prepareStatement(cekBarang);
                cekStm.setInt(1, ubh.barang_id);
                ResultSet cekRs = cekStm.executeQuery();
                cekRs.next();
                if (cekRs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(null, "Barang ID tidak ditemukan dalam tabel barang.");
                    return;
                }
                cekRs.close();
                
                String updateDetailBarang = "UPDATE detailbarang SET harga_jual = ? WHERE barang_id = ?";
                this.stm = conn.getConnection().prepareStatement(updateDetailBarang);
                this.stm.setDouble(1, ubh.harga_baru);
                this.stm.setInt(2, ubh.barang_id);
                this.stm.executeUpdate();
                this.stm.close();
                
                String insertRevisiHarga = "INSERT INTO revisihargajual (tanggal_revisi, barang_id, "
                        + "harga_jual_lama, harga_jual_baru, keterangan) VALUES (?, ?, ?, ?, ?)";
                this.sta = conn.getConnection().prepareStatement(insertRevisiHarga);
                java.sql.Date sqlDate = new java.sql.Date(ubh.tanggal_ubah.getTime());
                this.sta.setDate(1, sqlDate);
                this.sta.setInt(2, ubh.barang_id);
                this.sta.setDouble(3, ubh.harga_lama);
                this.sta.setDouble(4, ubh.harga_baru);
                this.sta.setString(5, ubh.keterangan);
                this.sta.executeUpdate();
                this.sta.close();
                
                JOptionPane.showMessageDialog(null, "Transaksi Ubah Harga Berhasil!");

                Thread.sleep(1000);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(null, "Harap memasukkan data secara lengkap", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_prosesUbahBarangBtnMouseClicked

    private void hargaBaruTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_hargaBaruTxtFocusGained
        if(hargaBaruTxt.getText().equals("Input Harga Jual Baru")) {
            hargaBaruTxt.setText(null);
            hargaBaruTxt.requestFocus();
            // Remove placeholder style
            removePlaceHolderStyle(hargaBaruTxt);
        }
    }//GEN-LAST:event_hargaBaruTxtFocusGained

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
                    hargaLamaTxt.setText(String.valueOf(rs.getDouble("harga_jual")));
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

    private void UbahHargaTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UbahHargaTableMouseClicked
        try {
            int selectedRow = UbahHargaTable.getSelectedRow();
            if (selectedRow != -1) {
                String tanggal_ubah = (String) model.getValueAt(selectedRow, 1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date tanggalUbah = sdf.parse(tanggal_ubah); 
                Object kode_barang = model.getValueAt(selectedRow, 2);
                Object nama_barang = model.getValueAt(selectedRow, 3);
                Object hargaLama = model.getValueAt(selectedRow, 4);
                Object hargaBaru = model.getValueAt(selectedRow, 5);
                Object keterangan = model.getValueAt(selectedRow, 6);
                
                tanggalUbahChooser.setDate(tanggalUbah);
                namaBarangTxt.setText(nama_barang != null ? nama_barang.toString() : "");
                kodeBarangComboBox.setSelectedItem(kode_barang != null ? kode_barang.toString() : "");
                namaBarangTxt.setText(nama_barang != null ? nama_barang.toString() : "");
                hargaLamaTxt.setText(hargaLama != null ? hargaLama.toString() : "");
                hargaBaruTxt.setText(hargaBaru != null ? hargaBaru.toString() : "");
                keteranganTextBox.setText(keterangan != null ? keterangan.toString() : "");
            } else {
                JOptionPane.showMessageDialog(null, "No row selected.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_UbahHargaTableMouseClicked

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
            java.util.logging.Logger.getLogger(UbahHarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UbahHarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UbahHarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UbahHarga.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UbahHarga().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable UbahHargaTable;
    private javax.swing.JTextField hargaBaruTxt;
    private javax.swing.JTextField hargaLamaTxt;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton kelolaBarangBtn;
    private javax.swing.JTextArea keteranganTextBox;
    private javax.swing.JComboBox<String> kodeBarangComboBox;
    private javax.swing.JButton logoutBtn5;
    private javax.swing.JTextField namaBarangTxt;
    private javax.swing.JButton pemutihanBtn;
    private javax.swing.JButton prosesUbahBarangBtn;
    private javax.swing.JButton salesBtn5;
    private com.toedter.calendar.JDateChooser tanggalUbahChooser;
    private javax.swing.JLabel transaksiUbahHargaTxt;
    private javax.swing.JButton ubahHargaBtn;
    // End of variables declaration//GEN-END:variables
}
