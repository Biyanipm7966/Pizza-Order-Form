import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {

    private JRadioButton thinCrust, regularCrust, deepDishCrust;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppings;
    private JTextArea orderDisplay;
    private JButton orderButton, clearButton, quitButton;

    private final double[] sizePrices = {8.00, 12.00, 16.00, 20.00};
    private final double toppingPrice = 1.00;
    private final double taxRate = 0.07;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());

        add(createCrustPanel(), BorderLayout.WEST);
        add(createSizePanel(), BorderLayout.CENTER);
        add(createToppingsPanel(), BorderLayout.EAST);
        add(createOrderDisplayPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.SOUTH);

        // Adding action listeners
        orderButton.addActionListener(new OrderAction());
        clearButton.addActionListener(new ClearAction());
        quitButton.addActionListener(new QuitAction());
    }

    private JPanel createCrustPanel() {
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(new TitledBorder("Select Crust Type"));
        crustPanel.setLayout(new BoxLayout(crustPanel, BoxLayout.Y_AXIS));

        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");

        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);

        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDishCrust);

        return crustPanel;
    }

    private JPanel createSizePanel() {
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(new TitledBorder("Select Size"));
        sizePanel.setLayout(new FlowLayout());

        String[] sizes = {"Small", "Medium", "Large", "Super Large"};
        sizeComboBox = new JComboBox<>(sizes);

        sizePanel.add(sizeComboBox);

        return sizePanel;
    }

    private JPanel createToppingsPanel() {
        JPanel toppingsPanel = new JPanel();
        toppingsPanel.setBorder(new TitledBorder("Select Toppings"));
        toppingsPanel.setLayout(new GridLayout(3, 2));

        String[] toppingNames = {"Cheese", "Pepperoni", "Mushrooms", "Olives", "Bacon", "Pineapple"};
        toppings = new JCheckBox[toppingNames.length];

        for (int i = 0; i < toppingNames.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            toppingsPanel.add(toppings[i]);
        }

        return toppingsPanel;
    }

    private JPanel createOrderDisplayPanel() {
        JPanel displayPanel = new JPanel();
        displayPanel.setBorder(new TitledBorder("Order Details"));
        displayPanel.setLayout(new BorderLayout());

        orderDisplay = new JTextArea(8, 40);
        orderDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderDisplay);

        displayPanel.add(scrollPane);

        return displayPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        return buttonPanel;
    }

    private class OrderAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Compile the order
            StringBuilder order = new StringBuilder("=========================================\n");
            double subTotal = 0.0;

            // Crust type
            String crust = null;
            if (thinCrust.isSelected()) {
                crust = "Thin Crust";
            } else if (regularCrust.isSelected()) {
                crust = "Regular Crust";
            } else if (deepDishCrust.isSelected()) {
                crust = "Deep-dish Crust";
            }

            if (crust == null) {
                JOptionPane.showMessageDialog(PizzaGUIFrame.this, "Please select a crust type.");
                return;
            }

            // Pizza size
            int sizeIndex = sizeComboBox.getSelectedIndex();
            String size = (String) sizeComboBox.getSelectedItem();
            double sizePrice = sizePrices[sizeIndex];
            subTotal += sizePrice;

            order.append(String.format("Type of Crust & Size: %s & %s - $%.2f\n", crust, size, sizePrice));

            // Toppings
            order.append("Toppings:\n");
            int selectedToppings = 0;
            for (JCheckBox topping : toppings) {
                if (topping.isSelected()) {
                    selectedToppings++;
                    order.append(topping.getText()).append(" - $").append(toppingPrice).append("\n");
                    subTotal += toppingPrice;
                }
            }

            if (selectedToppings == 0) {
                JOptionPane.showMessageDialog(PizzaGUIFrame.this, "Please select at least one topping.");
                return;
            }

            // Calculate totals
            order.append("=========================================\n");
            order.append(String.format("Sub-total: $%.2f\n", subTotal));
            double tax = subTotal * taxRate;
            order.append(String.format("Tax (7%%): $%.2f\n", tax));
            double total = subTotal + tax;
            order.append("----------------------------------------------------\n");
            order.append(String.format("Total: $%.2f\n", total));
            order.append("=========================================\n");

            // Display the order
            orderDisplay.setText(order.toString());
        }
    }

    private class ClearAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Clear form
            thinCrust.setSelected(false);
            regularCrust.setSelected(false);
            deepDishCrust.setSelected(false);
            sizeComboBox.setSelectedIndex(0);
            for (JCheckBox topping : toppings) {
                topping.setSelected(false);
            }
            orderDisplay.setText("");
        }
    }

    private class QuitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Confirm quit
            int response = JOptionPane.showConfirmDialog(PizzaGUIFrame.this,
                    "Are you sure you want to quit?", "Confirm Quit", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PizzaGUIFrame frame = new PizzaGUIFrame();
                frame.setVisible(true);
            }
        });
    }
}
