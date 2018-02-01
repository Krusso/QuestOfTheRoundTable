namespace Card
{
    public class Card
    {
        protected string Name {
            get { return Name; }
            set { Name = value; }
        }
        public Card()
        {

        }
        public Card(string name)
        {
            Name = name;
        }
    }
}

