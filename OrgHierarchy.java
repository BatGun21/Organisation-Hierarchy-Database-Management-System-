import java.io.*; 
import java.util.*; 

// Tree node
class Node{
	Node leftChild;
	Node rightChild;
	int height; 

	int id;
	int level;
	int bossId;
	ArrayList<Node>nodesEmployee = new ArrayList<Node>();
    Node(int value){
		id = value;
		height = 1;
	}
	public void traverseInOrder() {
        if (leftChild != null) {
            leftChild.traverseInOrder();
        }
        System.out.print(id + ", ");
        if (rightChild != null) {
            rightChild.traverseInOrder();
        }
    }
}
class AVLTree{
	// Construction of dummy root.
	Node root = new Node(0);

	public int height(Node a){
		return (a==null) ? 0 : a.height;
	}

	public int BalanceAtNode(Node a){
		if (a==null){return 0;}
		else{
			return height(a.leftChild)-height(a.rightChild);
		}
	}

	public Node find(Node a,int id) throws EmptyTreeException{
		if (a==null){
			throw new EmptyTreeException("Node is Empty");
		}if (id == a.id){
			return a;
		}else if (id<a.id){
			if (a.leftChild==null){
				return null;
			}else{
				return find(a.leftChild,id);
			}
		}else{
			if (a.rightChild==null){
				return null;
			}else{
				return find(a.rightChild,id);
			}
		}
	}
	public Node insert(Node a,int id){
		if (a == null)
			return (new Node(id));
		
		if (id < a.id)
			a.leftChild = insert(a.leftChild, id);
		
		else if (id>a.id)
			a.rightChild = insert(a.rightChild, id);
		else
			return a;
		
		a.height = 1 + Math.max(height(a.leftChild),height(a.rightChild));

		int balance = BalanceAtNode(a);

		if (balance>1 && id < a.leftChild.id){
			return rightRotation(a);
		}
		if (balance>1 && id > a.leftChild.id){
			a.leftChild = leftRotation(a.leftChild);
			return rightRotation(a);
		}
		if (balance<-1 && id > a.rightChild.id){
			return leftRotation(a);
		}
		//System.out.println(a.rightChild==null);
		if (balance<-1 && id < a.rightChild.id){
			a.rightChild = rightRotation(a.rightChild);
			return leftRotation(a);
		}
		return a;
	}
	public Node deleteNode(Node a, int id){
		if (a == null)
			return a;
		
		if (id<a.id)
			a.leftChild = deleteNode(a.leftChild, id);
		else if (id>a.id)
			a.rightChild = deleteNode(a.rightChild, id);

		else{
			if (a.leftChild==null||a.rightChild==null){
				Node temp = null;
				if(temp == a.leftChild)
					temp = a.rightChild;
				else
					temp = a.leftChild;
				
				if(temp==null){
					temp = a;
					a = null;
				}else
					a = temp;
			}else{
				Node temp = minValueNode(a.rightChild);
				a.id = temp.id;
				a.level=temp.level;
				a.bossId=temp.bossId;
				a.nodesEmployee=temp.nodesEmployee;
				a.rightChild = deleteNode(a.rightChild, temp.id);
			}
		}
		if (a==null)
			return a;
		
			a.height = Math.max(height(a.leftChild), height(a.rightChild))+1;

			int balance = BalanceAtNode(a);

			if (balance>1&&BalanceAtNode(a.leftChild)>=0)
				return rightRotation(a);
			
			if (balance>1&&BalanceAtNode(a)<0){
				a.leftChild = leftRotation(a.leftChild);
				return rightRotation(a);
			}

			if (balance<-1&&BalanceAtNode(a.rightChild)<=0)
				return leftRotation(a);

			if (balance<-1&&BalanceAtNode(a.rightChild)>0){
				a.rightChild = rightRotation(a.rightChild);
				return leftRotation(a);
			}

			return a;
			
	}

	public Node rightRotation(Node z){

		Node y = z.leftChild;
        Node T = y.rightChild;

        y.rightChild = z;
        z.leftChild = T;
 
        z.height=Math.max(height(z.leftChild),height(z.rightChild))+1;
        y.height=Math.max(height(y.leftChild),height(y.rightChild))+1;

		return y;
	}
	public Node leftRotation(Node z){
		Node y = z.rightChild;
        Node T = y.leftChild;

        y.leftChild = z;
        z.rightChild = T;
 
        z.height = Math.max(height(z.leftChild),height(z.rightChild))+1;
        y.height = Math.max(height(y.leftChild),height(y.rightChild))+1;

		return y;
	}
	public Node minValueNode(Node a){
		Node currentNode = a;
		while(currentNode.leftChild != null){
			currentNode = currentNode.leftChild;
		}return currentNode;
	}
	public void traverseInOrder() {
        if (root != null) {
            root.traverseInOrder();
        }
    }
	

}


public class OrgHierarchy implements OrgHierarchyInterface{

AVLTree orgTree = new AVLTree();
int size = 0;



public boolean isEmpty(){
	if (size == 0){
		return true;
	}else return false;	
} 

public int size(){
	return size;
}

public int level(int id) throws IllegalIDException, EmptyTreeException{
	if (isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	Node found = orgTree.find(orgTree.root,id);
	if (found==null){
		throw new IllegalIDException("Invalid id");
	}
	return found.level;
	
} 

public void hireOwner(int id) throws NotEmptyException{
	if (isEmpty()){
		orgTree.root.id = id;
		orgTree.root.level = 1;
		orgTree.root.bossId = -1;
		size++;
	}else{
		throw new NotEmptyException("Tree is not Empty");
	}
}

public void hireEmployee(int id, int bossid) throws IllegalIDException, EmptyTreeException{
	if (isEmpty()){
		throw new EmptyTreeException("Tree does not have a owner");
	}
	Node found = orgTree.find(orgTree.root, id);
	if (found!=null){
		throw new IllegalIDException("Duplicate id is not allowed");
	}
	Node bossNode = orgTree.find(orgTree.root, bossid);
	if (bossNode==null){
		throw new IllegalIDException("Invalid bossid");
	}
	orgTree.root = orgTree.insert(orgTree.root, id);

	Node newEmployeeNode = orgTree.find(orgTree.root, id);

	bossNode.nodesEmployee.add(newEmployeeNode);
	newEmployeeNode.level = bossNode.level+1;
	newEmployeeNode.bossId = bossid;
	size++;
} 

public void fireEmployee(int id) throws IllegalIDException,EmptyTreeException{
	if(isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	Node found = orgTree.find(orgTree.root, id);

	if (found==null){
		throw new IllegalIDException("Invalid id");
	}
	if(found.level==1){
		throw new IllegalIDException("Owner cannot be deleted");

	}

	if (found.nodesEmployee.size()!=0){
		throw new IllegalIDException("This Node has employees under it");
	}
	Node bossNode = orgTree.find(orgTree.root,found.bossId);
	bossNode.nodesEmployee.remove(found);

	orgTree.root = orgTree.deleteNode(orgTree.root, id);
	size--;


}
public void fireEmployee(int id, int manageid) throws IllegalIDException,EmptyTreeException{
	if(isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	Node found = orgTree.find(orgTree.root, id);
	if (found==null){
		throw new IllegalIDException("Invalid id");
	}
	if(found.level==1){
		throw new IllegalIDException("Owner cannot be deleted");
	}

	Node manageNode = orgTree.find(orgTree.root, manageid);
	if (manageNode==null){
		throw new IllegalIDException("Invalid manageid");
	}
	Node bossNode = orgTree.find(orgTree.root,found.bossId);
	bossNode.nodesEmployee.remove(found);
	ArrayList<Node>transferEmployee = new ArrayList<Node>();
	transferEmployee = found.nodesEmployee;
	manageNode.nodesEmployee.addAll(transferEmployee);
	if (manageNode.level!=found.level){ 
		for(Node node: manageNode.nodesEmployee){
			node.level = manageNode.level+1;
		}
	}
	for(Node node: manageNode.nodesEmployee){
		node.bossId = manageNode.id;
	}
	orgTree.root = orgTree.deleteNode(orgTree.root, id);
	size--;
} 

public int boss(int id) throws IllegalIDException,EmptyTreeException{
	if (isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	Node found = orgTree.find(orgTree.root,id);
	if (found==null){
		throw new IllegalIDException("Invalid id");
	}
	return found.bossId;
}

public int lowestCommonBoss(int id1, int id2) throws IllegalIDException,EmptyTreeException{
	Node node1 = orgTree.find(orgTree.root, id1);
	Node node2 = orgTree.find(orgTree.root, id2);

	Node boss1 = orgTree.find(orgTree.root, node1.bossId);
	Node boss2 = orgTree.find(orgTree.root, node2.bossId);
	if (isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	if(node1==null||node2==null){
		throw new IllegalIDException("Invalid id");
	}
	if (node1.level==1||node2.level==1){
		return -1;
	}
	if (boss1.id==boss2.id){
		return boss1.id;
	}
	if (node1.level>node2.level){
		return lowestCommonBoss(boss1.id, id2);
	}else if(node1.level<node2.level){
		return lowestCommonBoss(id1, boss2.id);
	}else{
		return lowestCommonBoss(boss1.id, boss2.id);
	} 
}

public String toString(int id) throws IllegalIDException, EmptyTreeException{
	String output = new String();
	if (isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	Node found = orgTree.find(orgTree.root, id);
	if (found==null){
		throw new IllegalIDException("Invalid id");
	}
	output = output + id;
	ArrayList<Node> temp1 = found.nodesEmployee;
	do{
		output = output +","+ helperToString(temp1);
		ArrayList<Node> temp3 = new ArrayList<Node>();
		for (Node node:temp1){
			if(node.nodesEmployee!=null){
				temp3.addAll(node.nodesEmployee);
			}	
		}
		temp1 = temp3;
	}while (temp1.size()!=0);

	return output;
	

}
public String helperToString(ArrayList<Node> tempList){
	ArrayList<Integer> temp2 = new ArrayList<Integer>();
	for (Node node: tempList){
		temp2.add(node.id);
	}
	Collections.sort(temp2);
	String outString = new String();
	for(int sortIds:temp2){
		outString = outString + " " + sortIds;
	}
	outString=outString.trim();
	return outString;		

}
}
