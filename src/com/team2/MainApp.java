package com.team2;

import com.team2.solutions.arrays.ArraySolutions;
import com.team2.solutions.lists.LinkedListSolutions;
import com.team2.solutions.math.MathSolutions;
import com.team2.solutions.strings.StringSolutions;
import com.team2.solutions.util.ListNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class MainApp extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextArea resultsArea;

    private ArraySolutions arraySolutions = new ArraySolutions();
    private LinkedListSolutions linkedListSolutions = new LinkedListSolutions();
    private MathSolutions mathSolutions = new MathSolutions();
    private StringSolutions stringSolutions = new StringSolutions();

    public MainApp() {
        setTitle("Team 2 Solution Explorer");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        resultsArea = new JTextArea(20, 50);
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane resultsScrollPane = new JScrollPane(resultsArea);
        resultsScrollPane.setBorder(BorderFactory.createTitledBorder("Results"));

        mainPanel.add(new MenuPanel(), "MENU");
        mainPanel.add(new ArrayPanel(), "ARRAY");
        mainPanel.add(new LinkedListPanel(), "LIST");
        mainPanel.add(new MathPanel(), "MATH");
        mainPanel.add(new StringPanel(), "STRING");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        bottomPanel.add(resultsScrollPane, BorderLayout.CENTER);

        JButton clearResultsButton = new JButton("Clear Results");
        clearResultsButton.addActionListener(e -> resultsArea.setText(""));
        bottomPanel.add(clearResultsButton, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        cardLayout.show(mainPanel, "MENU");
    }

    private void showOnScreen(String text) {
        resultsArea.append(text + "\n");
    }

    private String listToString(ListNode node) {
        if (node == null) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (node != null) {
            sb.append(node.val);
            if (node.next != null) {
                sb.append(" -> ");
            }
            node = node.next;
        }
        sb.append("]");
        return sb.toString();
    }

    private int[] parseStringToIntArray(String s) throws NumberFormatException {
        if (s == null || s.trim().isEmpty()) {
            return new int[0];
        }
        String[] parts = s.split("[,\\s]+");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i].trim());
        }
        return arr;
    }

    private ListNode parseStringToListNode(String s) throws NumberFormatException {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        String[] parts = s.split("[,\\s]+");
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        for (String part : parts) {
            current.next = new ListNode(Integer.parseInt(part.trim()));
            current = current.next;
        }
        return dummy.next;
    }

    private String[] parseStringToStringArray(String s) {
        if (s == null || s.trim().isEmpty()) {
            return new String[0];
        }
        return s.split("[,\\s]+");
    }

    private int[][] parseStringTo2DIntArray(String s) throws NumberFormatException {
        if (s == null || s.trim().isEmpty()) {
            return new int[0][0];
        }
        String[] rows = s.split("\\]\\s*,\\s*\\[");
        rows[0] = rows[0].replaceFirst("\\[\\[", "");
        rows[rows.length - 1] = rows[rows.length - 1].replaceFirst("\\]\\]", "");

        int numRows = rows.length;
        int[] firstRow = parseStringToIntArray(rows[0]);
        int numCols = firstRow.length;
        int[][] matrix = new int[numRows][numCols];
        matrix[0] = firstRow;

        for (int i = 1; i < numRows; i++) {
            matrix[i] = parseStringToIntArray(rows[i]);
            if(matrix[i].length != numCols) {
                throw new NumberFormatException("All rows must have the same number of columns.");
            }
        }
        return matrix;
    }

    private abstract class BaseSolutionPanel extends JPanel {
        public BaseSolutionPanel(String title) {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            add(titleLabel, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel(new GridBagLayout());
            add(new JScrollPane(buttonPanel), BorderLayout.CENTER);

            JButton backButton = new JButton("Back to Main Menu");
            backButton.addActionListener(e -> cardLayout.show(mainPanel, "MENU"));
            add(backButton, BorderLayout.SOUTH);

            createButtons(buttonPanel);
        }

        abstract void createButtons(JPanel panel);

        protected void addButton(JPanel panel, String text, Runnable action, GridBagConstraints gbc) {
            JButton button = new JButton(text);
            button.addActionListener(e -> {
                showOnScreen("--- Running: " + text + " ---");
                try {
                    action.run();
                } catch (NumberFormatException ex) {
                    showOnScreen("Error: Invalid number format. Please check your input.");
                    showOnScreen("Details: " + ex.getMessage());
                } catch (Exception ex) {
                    showOnScreen("An unexpected error occurred: " + ex.getMessage());
                }
                showOnScreen("------------------------------" + "\n");
            });
            panel.add(button, gbc);
        }
    }

    private class MenuPanel extends JPanel {
        public MenuPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            JLabel titleLabel = new JLabel("TEAM 2 PROJECT MAIN MENU");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(20, 20, 40, 20);
            add(titleLabel, gbc);

            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            String[] menuItems = {
                    "Array Solutions",
                    "Linked List Solutions",
                    "Math Solutions",
                    "String Solutions"
            };
            String[] panelKeys = {"ARRAY", "LIST", "MATH", "STRING"};

            for (int i = 0; i < menuItems.length; i++) {
                gbc.gridy = i + 1;
                JButton button = new JButton(menuItems[i]);
                button.setFont(new Font("Arial", Font.PLAIN, 16));
                final String key = panelKeys[i];
                button.addActionListener(e -> cardLayout.show(mainPanel, key));
                gbc.ipadx = 100;
                add(button, gbc);
            }

            JButton exitButton = new JButton("Exit");
            exitButton.addActionListener(e -> System.exit(0));
            gbc.gridy = menuItems.length + 1;
            gbc.insets = new Insets(30, 10, 20, 20);
            add(exitButton, gbc);
        }
    }

    private class ArrayPanel extends BaseSolutionPanel {
        public ArrayPanel() {
            super("Array Solutions");
        }

        @Override
        void createButtons(JPanel panel) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridy = 0;

            addButton(panel, "Find Median of Two Sorted Arrays", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter first sorted array (e.g., 1, 3)");
                if (s1 == null) return;
                String s2 = JOptionPane.showInputDialog(this, "Enter second sorted array (e.g., 2, 4)");
                if (s2 == null) return;

                int[] nums1 = parseStringToIntArray(s1);
                int[] nums2 = parseStringToIntArray(s2);

                double median = arraySolutions.findMedianSortedArrays(nums1, nums2);
                showOnScreen("Input: " + Arrays.toString(nums1) + ", " + Arrays.toString(nums2));
                showOnScreen("Median: " + median);
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Plus One", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter digits (e.g., 1, 2, 3 or 9, 9, 9)");
                if (s1 == null) return;

                int[] digits = parseStringToIntArray(s1);
                showOnScreen("Input: " + Arrays.toString(digits));
                int[] result = arraySolutions.plusOne(digits);
                showOnScreen("Result: " + Arrays.toString(result));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Remove Element", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter array (e.g., 3, 2, 2, 3)");
                if (s1 == null) return;
                String s2 = JOptionPane.showInputDialog(this, "Enter value to remove (e.g., 3)");
                if (s2 == null) return;

                int[] nums = parseStringToIntArray(s1);
                int val = Integer.parseInt(s2.trim());
                showOnScreen("Input: " + Arrays.toString(nums) + ", val: " + val);
                int len = arraySolutions.removeElement(nums, val);
                showOnScreen("New length: " + len + ", Array: " + Arrays.toString(Arrays.copyOf(nums, len)));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Two Sum", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter array (e.g., 2, 7, 11, 15)");
                if (s1 == null) return;
                String s2 = JOptionPane.showInputDialog(this, "Enter target (e.g., 9)");
                if (s2 == null) return;

                int[] nums = parseStringToIntArray(s1);
                int target = Integer.parseInt(s2.trim());
                showOnScreen("Input: " + Arrays.toString(nums) + ", target: " + target);
                int[] result = arraySolutions.twoSum(nums, target);
                showOnScreen("Indices: " + Arrays.toString(result));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Search Range", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter sorted array (e.g., 5, 7, 7, 8, 8, 10)");
                if (s1 == null) return;
                String s2 = JOptionPane.showInputDialog(this, "Enter target (e.g., 8)");
                if (s2 == null) return;

                int[] nums = parseStringToIntArray(s1);
                int target = Integer.parseInt(s2.trim());
                showOnScreen("Input: " + Arrays.toString(nums) + ", target: " + target);
                int[] result = arraySolutions.searchRange(nums, target);
                showOnScreen("Range: " + Arrays.toString(result));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Can Jump", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter jump array (e.g., 2, 3, 1, 1, 4)");
                if (s1 == null) return;

                int[] nums = parseStringToIntArray(s1);
                showOnScreen("Input: " + Arrays.toString(nums));
                boolean result = arraySolutions.canJump(nums);
                showOnScreen("Can Jump: " + result);
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Max Area (Container with Most Water)", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter heights (e.g., 1, 8, 6, 2, 5, 4, 8, 3, 7)");
                if (s1 == null) return;

                int[] height = parseStringToIntArray(s1);
                showOnScreen("Input: " + Arrays.toString(height));
                int result = arraySolutions.maxArea(height);
                showOnScreen("Max Area: " + result);
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Remove Duplicates", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter sorted array (e.g., 1, 1, 2, 2, 3)");
                if (s1 == null) return;

                int[] nums = parseStringToIntArray(s1);
                showOnScreen("Input: " + Arrays.toString(nums));
                int len = arraySolutions.removeDuplicates(nums);
                showOnScreen("New length: " + len + ", Array: " + Arrays.toString(Arrays.copyOf(nums, len)));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Num Submat (Count Submatrices)", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter matrix (e.g., [[1,0,1],[1,1,0],[1,1,0]])");
                if (s1 == null) return;

                int[][] mat = parseStringTo2DIntArray(s1);
                showOnScreen("Input: " + Arrays.deepToString(mat));
                int result = arraySolutions.numSubmat(mat);
                showOnScreen("Number of Submatrices: " + result);
            }, gbc);
        }
    }

    private class LinkedListPanel extends BaseSolutionPanel {
        public LinkedListPanel() {
            super("Linked List Solutions");
        }

        @Override
        void createButtons(JPanel panel) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridy = 0;

            addButton(panel, "Add Two Numbers", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter first list (e.g., 2, 4, 3)");
                if (s1 == null) return;
                String s2 = JOptionPane.showInputDialog(this, "Enter second list (e.g., 5, 6, 4)");
                if (s2 == null) return;

                ListNode l1 = parseStringToListNode(s1);
                ListNode l2 = parseStringToListNode(s2);
                showOnScreen("L1: " + listToString(l1));
                showOnScreen("L2: " + listToString(l2));
                ListNode actual = linkedListSolutions.addTwoNumbers(l1, l2);
                showOnScreen("Result: " + listToString(actual));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Merge K Lists", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter list 1 (e.g., 1, 4, 5)");
                if (s1 == null) return;
                String s2 = JOptionPane.showInputDialog(this, "Enter list 2 (e.g., 1, 3, 4)");
                if (s2 == null) return;
                String s3 = JOptionPane.showInputDialog(this, "Enter list 3 (e.g., 2, 6)");
                if (s3 == null) return;

                ListNode a1 = parseStringToListNode(s1);
                ListNode a2 = parseStringToListNode(s2);
                ListNode a3 = parseStringToListNode(s3);
                ListNode[] lists = {a1, a2, a3};

                showOnScreen("List 1: " + listToString(a1));
                showOnScreen("List 2: " + listToString(a2));
                showOnScreen("List 3: " + listToString(a3));
                ListNode actual = linkedListSolutions.mergeKLists(lists);
                showOnScreen("Merged Result: " + listToString(actual));
            }, gbc);
        }
    }

    private class MathPanel extends BaseSolutionPanel {
        public MathPanel() {
            super("Math Solutions");
        }

        @Override
        void createButtons(JPanel panel) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridy = 0;

            addButton(panel, "Is Palindrome Number", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter a number (e.g., 121)");
                if (s1 == null) return;

                int num = Integer.parseInt(s1.trim());
                showOnScreen("Input: " + num + " → " + mathSolutions.isPalindrome(num));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Is Power of Four", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter a number (e.g., 16)");
                if (s1 == null) return;

                int n = Integer.parseInt(s1.trim());
                showOnScreen("Input: " + n + " → " + mathSolutions.isPowerOfFour(n));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Next Beautiful Number", () -> {
                String s1 = JOptionPane.showInputDialog(this, "Enter a number (e.g., 1)");
                if (s1 == null) return;

                int n = Integer.parseInt(s1.trim());
                showOnScreen("Input: " + n + " → Next Beautiful: " + mathSolutions.nextBeautifulNumber(n));
            }, gbc);
        }
    }

    private class StringPanel extends BaseSolutionPanel {
        public StringPanel() {
            super("String Solutions");
        }

        @Override
        void createButtons(JPanel panel) {
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridy = 0;

            addButton(panel, "Is Palindrome", () -> {
                String s = JOptionPane.showInputDialog(this, "Enter a string (e.g., A man, a plan, a canal: Panama)");
                if (s == null) return;

                showOnScreen("Input: \"" + s + "\" → " + stringSolutions.isPalindrome(s));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Length of Longest Substring", () -> {
                String s = JOptionPane.showInputDialog(this, "Enter a string (e.g., abcabcbb)");
                if (s == null) return;

                showOnScreen("Input: \"" + s + "\" → " + stringSolutions.lengthOfLongestSubstring(s));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Longest Common Prefix", () -> {
                String s = JOptionPane.showInputDialog(this, "Enter strings (e.g., flower, flow, flight)");
                if (s == null) return;

                String[] strs = parseStringToStringArray(s);
                showOnScreen("Input: " + Arrays.toString(strs) +
                        " → Prefix: " + stringSolutions.longestCommonPrefix(strs));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Roman To Integer", () -> {
                String s = JOptionPane.showInputDialog(this, "Enter a Roman Numeral (e.g., MCMXCIV)");
                if (s == null) return;

                showOnScreen("Roman: " + s + " → Integer: " + stringSolutions.romanToInt(s));
            }, gbc);

            gbc.gridy++;
            addButton(panel, "Find Substring (Concatenation of Words)", () -> {
                String s = JOptionPane.showInputDialog(this, "Enter the main string (e.g., barfoothefoobarman)");
                if (s == null) return;
                String w = JOptionPane.showInputDialog(this, "Enter the words (e.g., foo, bar)");
                if (w == null) return;

                String[] words = parseStringToStringArray(w);
                List<Integer> result = stringSolutions.findSubstring(s, words);
                showOnScreen("Input: \"" + s + "\", Words: " + Arrays.toString(words) +
                        " → Indices: " + result);
            }, gbc);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainApp().setVisible(true);
        });
    }
}