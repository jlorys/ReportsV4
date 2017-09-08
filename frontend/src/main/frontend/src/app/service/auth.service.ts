import {Injectable} from '@angular/core'
import {RequestOptions, Headers, Http} from '@angular/http';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class AuthService {
  private options = new RequestOptions({headers: new Headers({'Content-Type': 'application/x-www-form-urlencoded'})});

  constructor(private http: Http) {
  }

  isAuthenticated(): Observable<boolean> {
    return this.http.get('/api/authenticated').map(response => <boolean> response.json()).catch(this.handleError);
  }

  isLoggedUserHasRoleUser(): Observable<boolean> {
    return this.http.get('/api/isLoggedUserHasRoleUser').map(response => <boolean> response.json()).catch(this.handleError);
  }

  isLoggedUserHasRoleAdmin(): Observable<boolean> {
    return this.http.get('/api/isLoggedUserHasRoleAdmin').map(response => <boolean> response.json()).catch(this.handleError);
  }

  login(username: string, password: string): Observable<boolean> {
    console.log("login for " + username);
    let body = 'username=' + username + '&password=' + password + '&submit=Login';

    return this.http.post('/api/login', body, this.options).map(res => res.status == 200).catch(this.handleError);
  }

  // sample method from angular doc
  private handleError(error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `Status: ${error.status} - Text: ${error.statusText}` : 'Server error';
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}
