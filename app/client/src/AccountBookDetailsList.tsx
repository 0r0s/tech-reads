import * as React from 'react';
import './App.css';

interface AccountBookDetails {
  id: string;
  accountId: string;
  book: Book;
  createDate: Date;
  updateDate: Date;
  review: string;
  notes: Note[];
  paretoList: string[];
  chapters: Chapter[];
}

interface Book {
   id: string;
   title: string;
   author: string;
   categories: string[];
   tags: string[];
   createDate: Date;
   updateDate: Date;
}

interface Chapter {
   number: number;
   name: string;
   notes: Note[];
   paretoList: string[];
}

interface Note {
    content: string;
    createDate: Date;
    tags: string[];
}

interface AccountBookDetailsProps {

}

interface AccountBookDetailsState {
  accountBookDetails: AccountBookDetails[];
  isLoading: boolean;
}

class AccountBookDetailsList extends React.Component<AccountBookDetailsProps, AccountBookDetailsState> {

 constructor(props: AccountBookDetailsProps) {
    super(props);

    this.state = {
      accountBookDetails: [],
      isLoading: false
    };
  }

componentDidMount() {
  this.setState({isLoading: true});

  fetch('http://localhost:8500/books/api/details/account/1')
    .then(response => response.json())
    .then(data => this.setState({accountBookDetails: data, isLoading: false}));
}

  public render() {
    const {accountBookDetails, isLoading} = this.state;

    if (isLoading) {
        return <p>Loading...</p>;
    }

    return (
      <div>
        <h2>My books list</h2>
        {accountBookDetails.map((details: AccountBookDetails) =>
          <div key={details.id}>
            {details.book.title} by {details.book.author} - {details.review}
          </div>
        )}
      </div>
    );
   }
}

export default AccountBookDetailsList;