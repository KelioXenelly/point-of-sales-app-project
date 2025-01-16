
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class KelolaBarang extends javax.swing.JFrame {
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
     * Creates new form KelolaBarang
     */
    public KelolaBarang() {
        initComponents();
        conn.connect();
        if(conn.getConnection() == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            this.stm = conn.getConnection().prepareStatement("SELECT kode_barang FROM kodebarang");
            this.rs = this.stm.executeQuery();
            beliKodeBarang.addItem("Pilih sebuah barang");
            while(rs.next()) {
                beliKodeBarang.addItem(rs.getString("kode_barang"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaBarang.class.getName()).log(Level.SEVERE, null, ex);
        }
        refreshTable();
    }
    
    class barang extends KelolaBarang {
        String kode_barang, nama_barang;
        
        public barang() {
            kode_barang = buatKodeBarangTxt.getText();
            nama_barang = namaBarangTxt.getText();
        }
    }
    
    class pembelian extends KelolaBarang {
        String kode_barang, nama_barang, keterangan;
        double harga_beli, harga_jual;
        Date tanggal_beli;
        int qty, barang_id, detail_barang_id, kode_barang_id;
        double profit;
        
        public pembelian() {
            try {
                kode_barang = beliKodeBarang.getSelectedItem().toString();
                tanggal_beli = beliTanggalBeliChooser.getDate();
                nama_barang = beliNamaBarangTxt.getText();
                qty = Integer.parseInt(beliQtyTxt.getText());
                harga_beli = Double.parseDouble(beliHargaBeliTxt.getText());
                harga_jual = Double.parseDouble(beliHargaJualTxt.getText());
                keterangan = keteranganTextBox.getText();
                profit = (harga_jual * qty) - (harga_beli * qty);
                
                this.stm = conn.getConnection().prepareStatement("SELECT barang.barang_id, kodebarang.kode_barang, barang.kode_barang_id "
                                                               + "FROM barang "
                                                               + "INNER JOIN kodebarang ON kodebarang.kode_barang_id = barang.kode_barang_id");
                this.rs = this.stm.executeQuery();
                
                barang_id = -1; // Default value if no match is found
                kode_barang_id = -1;
                
                while(this.rs.next()) {
                    if(kode_barang.equals(this.rs.getString("kode_barang"))) {
                        barang_id = this.rs.getInt("barang_id");
                        kode_barang_id = this.rs.getInt("kode_barang_id");
                        break; // Exit loop after finding the match
                    }
                }
                
                if (barang_id == -1) {
                    JOptionPane.showMessageDialog(null, "Barang ID not found for Kode Barang: " + kode_barang);
                }
                
                if (kode_barang_id == -1) {
                    JOptionPane.showMessageDialog(null, "kode_barang_id found for Kode Barang: " + kode_barang);
                }
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
        buatKodeBarangTxt.setText("Buat Kode Barang");
        namaBarangTxt.setText("Masukkan Nama Barang");
        beliKodeBarang.setSelectedItem("Pilih sebuah barang");
        beliTanggalBeliChooser.setDate(null);
        beliNamaBarangTxt.setText("");
        beliHargaBeliTxt.setText("");
        beliQtyTxt.setText("");
        beliHargaJualTxt.setText("");
        keteranganTextBox.setText("Tulis Keterangan");
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
        jPanel7 = new javax.swing.JPanel();
        tabelBarangLbl = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        namaBarangTxt = new javax.swing.JTextField();
        tambahBarangBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableBarang = new javax.swing.JTable();
        buatKodeBarangTxt = new javax.swing.JTextField();
        ubahBarangBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        transaksiBarangTxt = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        beliNamaBarangTxt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        beliHargaBeliTxt = new javax.swing.JTextField();
        beliQtyTxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        beliHargaJualTxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        beliKodeBarang = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        keteranganTextBox = new javax.swing.JTextArea();
        beliTanggalBeliChooser = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        prosesBeliBarang = new javax.swing.JButton();
        ubahBarang = new javax.swing.JButton();
        hapusBarang = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel19 = new javax.swing.JLabel();
        salesBtn5 = new javax.swing.JButton();
        kelolaBarangBtn = new javax.swing.JButton();
        pemutihanBtn = new javax.swing.JButton();
        kelolaPenggunaBtn = new javax.swing.JButton();
        logoutBtn5 = new javax.swing.JButton();
        ubahHargaBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tabelBarangLbl.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tabelBarangLbl.setText("Tabel Barang");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Kode Barang:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Nama Barang:");

        namaBarangTxt.setText("Masukkan Nama Barang");
        namaBarangTxt.setToolTipText("");
        namaBarangTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                namaBarangTxtFocusGained(evt);
            }
        });

        tambahBarangBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tambahBarangBtn.setText("Tambah");
        tambahBarangBtn.setAlignmentY(0.0F);
        tambahBarangBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tambahBarangBtnMouseClicked(evt);
            }
        });

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
        jScrollPane1.setViewportView(tableBarang);
        if (tableBarang.getColumnModel().getColumnCount() > 0) {
            tableBarang.getColumnModel().getColumn(0).setPreferredWidth(30);
            tableBarang.getColumnModel().getColumn(0).setMaxWidth(30);
            tableBarang.getColumnModel().getColumn(1).setPreferredWidth(80);
            tableBarang.getColumnModel().getColumn(1).setMaxWidth(80);
            tableBarang.getColumnModel().getColumn(2).setPreferredWidth(150);
            tableBarang.getColumnModel().getColumn(3).setPreferredWidth(30);
            tableBarang.getColumnModel().getColumn(3).setMaxWidth(30);
            tableBarang.getColumnModel().getColumn(4).setPreferredWidth(80);
            tableBarang.getColumnModel().getColumn(4).setMaxWidth(80);
            tableBarang.getColumnModel().getColumn(5).setPreferredWidth(80);
            tableBarang.getColumnModel().getColumn(5).setMaxWidth(80);
            tableBarang.getColumnModel().getColumn(6).setPreferredWidth(80);
            tableBarang.getColumnModel().getColumn(6).setMaxWidth(80);
            tableBarang.getColumnModel().getColumn(7).setMinWidth(0);
            tableBarang.getColumnModel().getColumn(7).setPreferredWidth(0);
            tableBarang.getColumnModel().getColumn(7).setMaxWidth(0);
        }

        buatKodeBarangTxt.setText("Buat Kode Barang");
        buatKodeBarangTxt.setToolTipText("");
        buatKodeBarangTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buatKodeBarangTxtFocusGained(evt);
            }
        });

        ubahBarangBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        ubahBarangBtn.setText("Ubah");
        ubahBarangBtn.setAlignmentY(0.0F);
        ubahBarangBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ubahBarangBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tabelBarangLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buatKodeBarangTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(namaBarangTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                                .addComponent(tambahBarangBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ubahBarangBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(tabelBarangLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buatKodeBarangTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ubahBarangBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tambahBarangBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namaBarangTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        transaksiBarangTxt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        transaksiBarangTxt.setText("Transaksi Pembelian Barang");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Kode Barang:");

        beliNamaBarangTxt.setToolTipText("");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Nama Barang:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Harga Beli:");

        beliHargaBeliTxt.setToolTipText("");

        beliQtyTxt.setToolTipText("");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Qty:");

        beliHargaJualTxt.setToolTipText("");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Harga Jual:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Keterangan:");

        beliKodeBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beliKodeBarangActionPerformed(evt);
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

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Tanggal Beli:");

        prosesBeliBarang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        prosesBeliBarang.setText("Proses");
        prosesBeliBarang.setAlignmentY(0.0F);
        prosesBeliBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prosesBeliBarangMouseClicked(evt);
            }
        });

        ubahBarang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        ubahBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Edit.png"))); // NOI18N
        ubahBarang.setText("Ubah");
        ubahBarang.setAlignmentY(0.0F);
        ubahBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ubahBarangMouseClicked(evt);
            }
        });

        hapusBarang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        hapusBarang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Trash.png"))); // NOI18N
        hapusBarang.setAlignmentY(0.0F);
        hapusBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hapusBarangMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(beliHargaBeliTxt)
                    .addComponent(beliNamaBarangTxt, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(beliHargaJualTxt, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(prosesBeliBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ubahBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hapusBarang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(beliQtyTxt, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(transaksiBarangTxt)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(beliKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel15)
                                    .addComponent(beliTanggalBeliChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel9)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(transaksiBarangTxt)
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(beliTanggalBeliChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                    .addComponent(beliKodeBarang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(beliNamaBarangTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(13, 13, 13)
                .addComponent(beliQtyTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(beliHargaBeliTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(beliHargaJualTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(prosesBeliBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ubahBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hapusBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(102, 153, 255));

        jLabel17.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Selamat datang,");

        jLabel19.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Admin");

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

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoutBtn5)
                .addGap(34, 34, 34))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salesBtn5)
                            .addComponent(kelolaBarangBtn)
                            .addComponent(pemutihanBtn)
                            .addComponent(kelolaPenggunaBtn)
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
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
    
    private void buatKodeBarangTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buatKodeBarangTxtFocusGained
        if(buatKodeBarangTxt.getText().equals("Buat Kode Barang")) {
            buatKodeBarangTxt.setText(null);
            buatKodeBarangTxt.requestFocus();
            // Remove placeholder style
            removePlaceHolderStyle(buatKodeBarangTxt);
        }
    }//GEN-LAST:event_buatKodeBarangTxtFocusGained

    private void namaBarangTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_namaBarangTxtFocusGained
        if(namaBarangTxt.getText().equals("Masukkan Nama Barang")) {
            namaBarangTxt.setText(null);
            namaBarangTxt.requestFocus();
            // Remove placeholder style
            removePlaceHolderStyle(namaBarangTxt);
        }
    }//GEN-LAST:event_namaBarangTxtFocusGained

    private void keteranganTextBoxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_keteranganTextBoxFocusGained
        if(keteranganTextBox.getText().equals("Tulis Keterangan")) {
            keteranganTextBox.setText(null);
            keteranganTextBox.requestFocus();
            // Remove placeholder style
            removePlaceHolderStyle(keteranganTextBox);
        }
    }//GEN-LAST:event_keteranganTextBoxFocusGained

    private void tambahBarangBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tambahBarangBtnMouseClicked
        try {
            barang brg = new barang();
            String insertKodeBarang = "INSERT IGNORE INTO kodebarang (kode_barang) VALUES (?)";
            String getKodeBarangId = "SELECT kode_barang_id FROM kodebarang WHERE kode_barang = ?";
            String insertBarang = "INSERT INTO barang (kode_barang_id, nama_barang) VALUES (?, ?)";
            String getKodeBarang = "SELECT kode_barang FROM kodebarang";
            
            if(!brg.kode_barang.isEmpty() && !brg.nama_barang.isEmpty()) {
                Connection connection = conn.getConnection();
                connection.setAutoCommit(false); // Start transaction
                
                // Check Kode Barang Exist in Database
                this.stg = connection.prepareStatement(getKodeBarang);
                this.rs = this.stg.executeQuery();
                
                while(rs.next()) {
                    if(brg.kode_barang.equals(rs.getString("kode_barang"))) {
                        JOptionPane.showMessageDialog(null, "Kode Barang sudah tersedia, harap masukkan kode baru", 
                                                            "Input Error", JOptionPane.WARNING_MESSAGE);
                        return; // Fixed: Stop execution if duplicate is found
                    }
                }
                
                // Insert Kode Barang
                this.stm = connection.prepareStatement(insertKodeBarang, Statement.RETURN_GENERATED_KEYS);
                stm.setString(1, brg.kode_barang);
                stm.executeUpdate();
                
                ResultSet rsGeneratedKeys = stm.getGeneratedKeys();
                int kodeBarangId;
                if (rsGeneratedKeys.next()) {
                    kodeBarangId = rsGeneratedKeys.getInt(1);  
                } else {
                    throw new SQLException("Failed to retrieve generated Kode Barang ID!");
                }
                
                // Insert Barang
                this.sta = connection.prepareStatement(insertBarang, Statement.RETURN_GENERATED_KEYS);
                sta.setInt(1, kodeBarangId);
                sta.setString(2, brg.nama_barang);
                sta.executeUpdate();
                
                ResultSet rsBarangId = sta.getGeneratedKeys();
                if (rsBarangId.next()) {
                    int barangId = rsBarangId.getInt(1);  
                } else {
                    throw new SQLException("Barang ID tidak ditemukan!");
                }
                
                connection.commit(); // Commit transaction
                
                JOptionPane.showMessageDialog(null, "Barang berhasil di tambahkan");
                
                Thread.sleep(1000);
                refreshTable();

                KelolaBarang kelolaBarangPage = new KelolaBarang();
                kelolaBarangPage.setVisible(true); 
                kelolaBarangPage.setLocationRelativeTo(null);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "Harap memasukkan kode dan nama barang", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch(Exception e) {
            try {
                conn.getConnection().rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_tambahBarangBtnMouseClicked

    private void tableBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBarangMouseClicked
        try {
            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow != -1) {
                Object kodeBarang = model.getValueAt(selectedRow, 1);
                Object namaBarang = model.getValueAt(selectedRow, 2);
                Object qty = model.getValueAt(selectedRow, 3);
                Object hargaBeli = model.getValueAt(selectedRow, 4);
                Object hargaJual = model.getValueAt(selectedRow, 5);
                
                buatKodeBarangTxt.setText(kodeBarang != null ? kodeBarang.toString() : "");
                namaBarangTxt.setText(namaBarang != null ? namaBarang.toString() : "");
                beliKodeBarang.setSelectedItem(kodeBarang != null ? kodeBarang.toString() : "");
                beliNamaBarangTxt.setText(namaBarang != null ? namaBarang.toString() : "");
                beliQtyTxt.setText(qty != null ? qty.toString() : "");
                beliHargaBeliTxt.setText(hargaBeli != null ? hargaBeli.toString() : "");
                beliHargaJualTxt.setText(hargaJual != null ? hargaJual.toString() : "");
            } else {
                JOptionPane.showMessageDialog(null, "No row selected.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_tableBarangMouseClicked

    private void prosesBeliBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prosesBeliBarangMouseClicked
        try {
            pembelian pbl = new pembelian();
            if(!pbl.nama_barang.isEmpty() && pbl.tanggal_beli != null &&
               pbl.qty > 0 && pbl.harga_beli != 0 && pbl.harga_jual != 0) {
                // Periksa apakah barang dengan harga beli yang sama sudah ada
                String checkQuery = "SELECT barang_id, qty FROM detailbarang WHERE barang_id = ? AND harga_beli = ?";
                this.stm = conn.getConnection().prepareStatement(checkQuery);
                stm.setInt(1, pbl.barang_id);
                stm.setDouble(2, pbl.harga_beli);
                this.rs = stm.executeQuery();

                if (this.rs.next()) {
                    // Jika harga beli sama, update qty saja
                    int existingQty = this.rs.getInt("qty");
                    int updatedQty = existingQty + pbl.qty;

                    String updateQuery = "UPDATE detailbarang SET qty = ? WHERE barang_id = ? AND harga_beli = ?";
                    this.stm = conn.getConnection().prepareStatement(updateQuery);
                    stm.setInt(1, updatedQty);
                    stm.setInt(2, pbl.barang_id);
                    stm.setDouble(3, pbl.harga_beli);
                    stm.executeUpdate();
                } else {
                    // Jika harga beli berbeda, insert data baru
                    String insertQuery = "INSERT INTO detailbarang (barang_id, qty, harga_beli, harga_jual, profit) VALUES (?, ?, ?, ?, ?)";
                    this.stm = conn.getConnection().prepareStatement(insertQuery);
                    stm.setInt(1, pbl.barang_id);
                    stm.setInt(2, pbl.qty);
                    stm.setDouble(3, pbl.harga_beli);
                    stm.setDouble(4, pbl.harga_jual);
                    stm.setDouble(5, pbl.profit);
                    stm.executeUpdate();
                }
                
                this.stm = conn.getConnection().prepareStatement("insert into pembelian "
                        + "(tanggal_beli, barang_id, qty, harga_beli, harga_jual, profit, keterangan) "
                        + "values (?, ?, ?, ?, ?, ?, ?)");
                
                java.sql.Date sqlDate = new java.sql.Date(pbl.tanggal_beli.getTime());
                stm.setDate(1, sqlDate);
                stm.setInt(2, pbl.barang_id);
                stm.setInt(3, pbl.qty);
                stm.setDouble(4, pbl.harga_beli);
                stm.setDouble(5, pbl.harga_jual);
                stm.setDouble(6, pbl.profit);
                stm.setString(7, pbl.keterangan);
                stm.executeUpdate();
                
                JOptionPane.showMessageDialog(null, "Transaksi Pembelian Berhasil!");
                
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
    }//GEN-LAST:event_prosesBeliBarangMouseClicked

    private void ubahBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ubahBarangMouseClicked
        try {
            pembelian pbl = new pembelian();
            conn.getConnection().setAutoCommit(false);

            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow != -1) {
                Object qty = model.getValueAt(selectedRow, 3);
                Object hargaBeli = model.getValueAt(selectedRow, 4);
                int qtyTable = Integer.parseInt(qty.toString());
                double hargaBeliTable = Double.parseDouble(hargaBeli.toString());
                
                // Update Tabel Barang
                String updateBarang = "UPDATE barang SET nama_barang = ? WHERE barang_id = ?";
                this.stt = conn.getConnection().prepareStatement(updateBarang);
                this.stt.setString(1, pbl.nama_barang);
                this.stt.setInt(2, pbl.barang_id);
                this.stt.executeUpdate();
                
                // Fetch detail_barang_id
                this.stm = conn.getConnection().prepareStatement("SELECT detail_barang_id, qty, harga_beli "
                                                               + "FROM detailbarang where barang_id = ?");
                this.stm.setInt(1, pbl.barang_id);
                this.rt = this.stm.executeQuery();
                
                while(this.rt.next()) {
                    if(qtyTable == this.rt.getInt("qty") && hargaBeliTable == this.rt.getDouble("harga_beli")) {
                        pbl.detail_barang_id = this.rt.getInt("detail_barang_id");
                        break;
                    }
                }
                
                // If no match found
                if (pbl.detail_barang_id == 0) {
                    JOptionPane.showMessageDialog(null, "No matching detail_barang_id found!");
                    return;
                }
                
                // Update Tabel Detail Barang
                String updateDetailBarang = "UPDATE detailbarang SET qty = ?, harga_beli = ?, harga_jual = ?, profit = ? WHERE detail_barang_id = ?";
                this.sta = conn.getConnection().prepareStatement(updateDetailBarang);
                this.sta.setInt(1, pbl.qty);
                this.sta.setDouble(2, pbl.harga_beli);
                this.sta.setDouble(3, pbl.harga_jual);
                this.sta.setDouble(4, pbl.profit);
                this.sta.setInt(5, pbl.detail_barang_id);
                this.sta.executeUpdate();

                conn.getConnection().commit();


                JOptionPane.showMessageDialog(null, "Ubah Barang Berhasil");
                
                Thread.sleep(1000);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(null, "No row selected.");
            }
        } catch (SQLException e) {
            // If any error occurs, rollback the transaction
            try {
                conn.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(KelolaBarang.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                // Reset auto-commit to true
                conn.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_ubahBarangMouseClicked

    private void hapusBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hapusBarangMouseClicked
        try {
            pembelian pbl = new pembelian();
            conn.getConnection().setAutoCommit(false);
            
            if(!pbl.nama_barang.equals("") && pbl.qty != 0 && pbl.harga_beli != 0 && pbl.harga_jual != 0) {
                int selectedRow = tableBarang.getSelectedRow();
                if (selectedRow != -1) {
                    Object qty = model.getValueAt(selectedRow, 3);
                    Object hargaBeli = model.getValueAt(selectedRow, 4);
                    Object hargaJual = model.getValueAt(selectedRow, 5);
                    int qtyTable = Integer.parseInt(qty.toString());
                    double hargaBeliTable = Double.parseDouble(hargaBeli.toString());
                    double hargaJualTable = Double.parseDouble(hargaJual.toString());

                    int dialogBtn = JOptionPane.YES_NO_OPTION;
                    int confirmDelete = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus barang tersebut?", "Warning", dialogBtn);

                    if(confirmDelete == JOptionPane.YES_OPTION) {
                        // Fetch detail_barang_id
                        this.stm = conn.getConnection().prepareStatement("SELECT detail_barang_id, qty, harga_beli, harga_jual "
                                                                       + "FROM detailbarang where barang_id = ?");
                        this.stm.setInt(1, pbl.barang_id);
                        this.rt = this.stm.executeQuery();

                        while(this.rt.next()) {
                            if(qtyTable == this.rt.getInt("qty") && hargaBeliTable == this.rt.getDouble("harga_beli") && 
                               hargaJualTable == this.rt.getDouble("harga_jual")) {
                                pbl.detail_barang_id = this.rt.getInt("detail_barang_id");
                                break;
                            }
                        }

                        // If no match found
                        if (pbl.detail_barang_id == 0) {
                            JOptionPane.showMessageDialog(null, "No matching detail_barang_id found!");
                            return;
                        }
                        
                        // Hapus Selected Pembelian
                        String hapusPembelianBarang = "DELETE FROM pembelian WHERE barang_id = ? AND harga_beli = ?";
                        this.sth = conn.getConnection().prepareStatement(hapusPembelianBarang);
                        this.sth.setInt(1, pbl.barang_id);
                        this.sth.setDouble(2, pbl.harga_beli);
                        this.sth.executeUpdate();
                        
                        // Hapus Selected Detail Barang
                        String hapusDetailBarang = "DELETE FROM detailbarang WHERE detail_barang_id = ?";
                        this.stt = conn.getConnection().prepareStatement(hapusDetailBarang);
                        this.stt.setInt(1, pbl.detail_barang_id);
                        this.stt.executeUpdate();

                        // Hapus selected nama Barang
                        String hapusNamaBarang = "DELETE FROM barang WHERE barang_id = ?";
                        this.sta = conn.getConnection().prepareStatement(hapusNamaBarang);
                        this.sta.setInt(1, pbl.barang_id);
                        this.sta.executeUpdate();

                        // Hapus selected kode Barang
                        String hapusKodeBarang = "DELETE FROM kodebarang WHERE kode_barang_id = ?";
                        this.stg = conn.getConnection().prepareStatement(hapusKodeBarang);
                        this.stg.setInt(1, pbl.kode_barang_id);
                        this.stg.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Hapus Transaksi Berhasil");
                        conn.getConnection().commit();
                        Thread.sleep(1000);
                        refreshTable();
                    } else {
                       JOptionPane.showMessageDialog(null, "Transaksi Hapus Dibatalkan");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Harap pilih barang yang mau dihapus");
                }
            }
        } catch (SQLException e) {
            // If any error occurs, rollback the transaction
            try {
                conn.getConnection().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(KelolaBarang.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                // Reset auto-commit to true
                conn.getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_hapusBarangMouseClicked

    private void ubahBarangBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ubahBarangBtnMouseClicked
        try {
            barang brg = new barang();
            String updateKodeBarang = "UPDATE kodebarang SET kode_barang = ? WHERE kode_barang_id = ?";
            String updateNamaBarang = "UPDATE barang SET nama_barang = ? WHERE kode_barang_id = ?";
            int kode_barang_id = -1;
            
            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow != -1) {
                Object kodeBarang = model.getValueAt(selectedRow, 1);
                String kodeBarangTable = kodeBarang.toString();
                
                if(!brg.kode_barang.isEmpty() && !brg.nama_barang.isEmpty()) {
                    Connection connection = conn.getConnection();
                    connection.setAutoCommit(false); // Start transaction

                    // Get kode_barang_id
                    this.stm = connection.prepareStatement("SELECT kode_barang_id FROM kodebarang where kode_barang = ?");
                    this.stm.setString(1, kodeBarangTable);
                    this.rs = this.stm.executeQuery();
                    
                    if(this.rs.next()) {
                        kode_barang_id = rs.getInt("kode_barang_id");
                    } else {
                        JOptionPane.showMessageDialog(null, "Kode Barang tidak ditemukan!");
                        return; 
                    }
                    
                    // Update Kode Barang
                    this.stg = connection.prepareStatement(updateKodeBarang);
                    this.stg.setString(1, brg.kode_barang);
                    this.stg.setInt(2, kode_barang_id);
                    this.stg.executeUpdate();

                    // Update Nama Barang
                    this.stt = connection.prepareStatement(updateNamaBarang);
                    this.stt.setString(1, brg.nama_barang);
                    this.stt.setInt(2, kode_barang_id);
                    stt.executeUpdate();

                    connection.commit(); // Commit transaction

                    JOptionPane.showMessageDialog(null, "Kode dan Nama Barang berhasil di Ubah");
                    
                    Thread.sleep(1000);
                    refreshTable();
                    
                    KelolaBarang kelolaBarangPage = new KelolaBarang();
                    kelolaBarangPage.setVisible(true); 
                    kelolaBarangPage.setLocationRelativeTo(null);
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Harap memasukkan kode dan nama barang", "Input Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch(Exception e) {
            try {
                conn.getConnection().rollback(); // Rollback on error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_ubahBarangBtnMouseClicked

    private void beliKodeBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beliKodeBarangActionPerformed
        try {
            String kodeBarang = beliKodeBarang.getSelectedItem().toString();
            if(!kodeBarang.equals("Pilih sebuah barang")) {
                String sql = "SELECT kodebarang.kode_barang, barang.nama_barang "
                           + "FROM barang "
                           + "INNER JOIN kodebarang ON kodebarang.kode_barang_id = barang.kode_barang_id "
                           + "WHERE kodebarang.kode_barang = ?";

                this.stm = conn.getConnection().prepareStatement(sql);
                this.stm.setString(1, kodeBarang);
                this.rs = stm.executeQuery();

                if (rs.next()) {
                    beliNamaBarangTxt.setText(rs.getString("nama_barang"));
                } else {
                    beliNamaBarangTxt.setText("");
                }
            } else {
                beliNamaBarangTxt.setText("");

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_beliKodeBarangActionPerformed

    private void salesBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesBtn5MouseClicked
        SalesAdmin PointofSalesPage = new SalesAdmin();
        PointofSalesPage.setVisible(true);
        PointofSalesPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_salesBtn5MouseClicked

    private void kelolaBarangBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kelolaBarangBtnMouseClicked
        KelolaBarang kelolaBarangPage = new KelolaBarang();
        kelolaBarangPage.setVisible(true);
        kelolaBarangPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_kelolaBarangBtnMouseClicked

    private void pemutihanBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pemutihanBtnMouseClicked
        PemutihanBarang pemutihanPage = new PemutihanBarang();
        pemutihanPage.setVisible(true);
        pemutihanPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_pemutihanBtnMouseClicked

    private void kelolaPenggunaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kelolaPenggunaBtnMouseClicked
        KelolaPengguna kelolaPenggunaPage = new KelolaPengguna();
        kelolaPenggunaPage.setVisible(true);
        kelolaPenggunaPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_kelolaPenggunaBtnMouseClicked

    private void logoutBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtn5MouseClicked
        logout();
    }//GEN-LAST:event_logoutBtn5MouseClicked

    private void ubahHargaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ubahHargaBtnMouseClicked
        UbahHarga ubahHargaPage = new UbahHarga();
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
            java.util.logging.Logger.getLogger(KelolaBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KelolaBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KelolaBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KelolaBarang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KelolaBarang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField beliHargaBeliTxt;
    private javax.swing.JTextField beliHargaJualTxt;
    private javax.swing.JComboBox<String> beliKodeBarang;
    private javax.swing.JTextField beliNamaBarangTxt;
    private javax.swing.JTextField beliQtyTxt;
    private com.toedter.calendar.JDateChooser beliTanggalBeliChooser;
    private javax.swing.JTextField buatKodeBarangTxt;
    private javax.swing.JButton hapusBarang;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton kelolaBarangBtn;
    private javax.swing.JButton kelolaPenggunaBtn;
    private javax.swing.JTextArea keteranganTextBox;
    private javax.swing.JButton logoutBtn5;
    private javax.swing.JTextField namaBarangTxt;
    private javax.swing.JButton pemutihanBtn;
    private javax.swing.JButton prosesBeliBarang;
    private javax.swing.JButton salesBtn5;
    private javax.swing.JLabel tabelBarangLbl;
    private javax.swing.JTable tableBarang;
    private javax.swing.JButton tambahBarangBtn;
    private javax.swing.JLabel transaksiBarangTxt;
    private javax.swing.JButton ubahBarang;
    private javax.swing.JButton ubahBarangBtn;
    private javax.swing.JButton ubahHargaBtn;
    // End of variables declaration//GEN-END:variables
}
