public class Main {
    public static void main(String[] args) {
        AVLTree tree = new AVLTree();
        tree.root.id = 5;
        tree.root.level = 1;
        
        Node node = new Node(0);

        
        
        // System.out.println(tree.root.id);
        // System.out.println(tree.root.level);
        try {
            tree.insert(tree.root, 7);
            tree.insert(tree.root, 3);
            tree.insert(tree.root, 9);
            tree.insert(tree.root, 2);
            tree.insert(tree.root, 4);
            tree.insert(tree.root, 10);
            
        } catch (IllegalIDException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }tree.traverseInOrder();
    }
    
}
