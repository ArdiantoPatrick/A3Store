package Dashboard;

import DataMaster.FormLogin;
import DataMaster.MasterCustomer;
import DataTransaksi.RiwayatTrSupplier;
import DataTransaksi.TransaksiPenjualan;
import DataTransaksi.TransaksiService;
import DataTransaksi.TransaksiSupplier;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardKasir extends JFrame{
    public JPanel JPDashboardKasir;
    private JPanel PanelKiri;
    private JPanel PanelTengah;
    private JPanel PanelKiriAtas;
    private JPanel PanelButton;
    private JPanel PanelRole;
    private JButton transaksiPenjualanButton;
    private JButton transaksiSupplierButton;
    private JButton transaksiServiceButton;
    private JButton masterCustomerButton;
    private JPanel PanelForm;
    private JLabel lblJudul;
    private JButton btnLogOut;
    private JButton btnRiwayatTrSupplier;

    public void FrameConfig(){
        add(this.JPDashboardKasir);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public DashboardKasir(String value) {
        FrameConfig();
        transaksiPenjualanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                TransaksiPenjualan show = new TransaksiPenjualan(value);
                show.TransaksiPenjualan.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.TransaksiPenjualan);
                lblJudul.setText("TRANSAKSI PENJUALAN");
            }
        });
        transaksiSupplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                TransaksiSupplier show = new TransaksiSupplier(value);
                show.TransaksiSupplier.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.TransaksiSupplier);
                lblJudul.setText("TRANSAKSI SUPPLIER");
            }
        });
        transaksiServiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                TransaksiService show = new TransaksiService();
                show.TransaksiService.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.TransaksiService);
                lblJudul.setText("TRANSAKSI SERVICE");
            }
        });
        masterCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                MasterCustomer show = new MasterCustomer();
                show.JPCustomer.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPCustomer);
                lblJudul.setText("MASTER CUSTOMER");
            }
        });
        btnLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                opsi = JOptionPane.showConfirmDialog(null,"Apakah ingin Log Out ?",
                        "Konfirmasi", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                if (opsi != 0){
                    JOptionPane.showMessageDialog(null, "Log Out Dibatalkan!");
                }else {
                    JOptionPane.showMessageDialog(null, "Terima Kasih");
                    dispose();
                    FormLogin show = new FormLogin();
                    show.JPLogin.setVisible(true);
                }
            }
        });
        btnRiwayatTrSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                RiwayatTrSupplier show = new RiwayatTrSupplier();
                show.JpRiwayatTrSupplier.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JpRiwayatTrSupplier);
                lblJudul.setText("RIWAYAT TRANSAKSI SUPPLIER");
            }
        });
    }
}
