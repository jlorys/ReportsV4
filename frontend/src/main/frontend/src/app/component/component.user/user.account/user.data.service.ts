import {Injectable} from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {AppUser} from "../../component.admin/users/user";

@Injectable()
export class UserAccountDataService {

  private options = new RequestOptions({headers: new Headers({'Content-Type': 'application/json'})});

  constructor(private http: Http) {}

  getLoggedUser(): Observable<AppUser> {
    return this.http.get('/api/loggedUser/')
      .map(response => new AppUser(response.json()))
      .catch(this.handleError);
  }

  changePassword(oldPassword: string, newPassword: string, newPasswordRepeat: string): Observable<AppUser> {
    return this.http.put('/api/userAccount/changePassword/'+oldPassword+'/'+newPassword+'/'+newPasswordRepeat+'', this.options)
      .map(response => new AppUser(response.json()))
      .catch(this.handleError);
  }

  // method from angular documentation
  private handleError(error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `Status: ${error.status} - Text: ${error.statusText}` : 'Server error';
    console.error(errMsg); // log to console instead
    if (error.status === 401) {
      window.location.href = '/';
    }
    return Observable.throw(errMsg);
  }
}
