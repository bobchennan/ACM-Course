IEnumerable<t> ScanInOrder(Node<t> root)
{
   if(root.LeftNode != null)
   {
      foreach(T item in ScanInOrder(root.LeftNode))
      {
         yield return item;
      }
   }
 
   yield return root.Item;
 
   if(root.RightNode != null)
   {
      foreach(T item in ScanInOrder(root.RightNode))
      {
         yield return item;
      }
   }
}