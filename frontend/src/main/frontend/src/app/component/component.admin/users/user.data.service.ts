import {Injectable} from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import {AppUser} from "./user";
import {Observable} from "rxjs/Observable";
import {PageRequestByExample, PageResponse} from "../../../support/paging";
import {LazyLoadEvent} from "primeng/primeng";

@Injectable()
export class AppUserDataService {

  private options = new RequestOptions({headers: new Headers({'Content-Type': 'application/json'})});

  constructor(private http: Http) {}

  delete(id: any) {
    return this.http.delete('/api/users/' + id).catch(this.handleError);
  }

  deleteOneDayUnconfirmedUsers() {
    return this.http.delete('/api/users/deleteOneDayUnconfirmedUsers').catch(this.handleError);
  }

  /**
   * Load a page (for paginated datatable) of AppUser using the passed
   * user as an example for the search by example facility.
   */
  getPage(user: AppUser, event: LazyLoadEvent): Observable<PageResponse<AppUser>> {
    let req = new PageRequestByExample(user, event);
    let body = JSON.stringify(req);

    return this.http.post('/api/users/page', body, this.options)
      .map(response => {
        let pr: any = response.json();
        return new PageResponse<AppUser>(pr.totalPages, pr.totalElements, AppUser.toArray(pr.content));
      })
      .catch(this.handleError);
  }

  getUser(id: any): Observable<AppUser> {
    return this.http.get('/api/users/' + id)
      .map(response => new AppUser(response.json()))
      .catch(this.handleError);
  }

  getUserByUserName(userName: string): Observable<AppUser> {
    return this.http.get('/api/users/name/' + userName)
      .map(response => new AppUser(response.json()))
      .catch(this.handleError);
  }

  getLoggedUser(): Observable<AppUser> {
    return this.http.get('/api/loggedUser/')
      .map(response => new AppUser(response.json()))
      .catch(this.handleError);
  }

  update(user: AppUser): Observable<AppUser> {
    let body = JSON.stringify(user);

    return this.http.put('/api/users/', body, this.options)
      .map(response => new AppUser(response.json()))
      .catch(this.handleError);
  }

  register(user: AppUser): Observable<AppUser> {
    let body = JSON.stringify(user);

    return this.http.put('/api/userAccount/register/', body, this.options)
      .map(response => new AppUser(response.json()))
      .catch(this.handleError);
  }

  changePassword(userId: number, oldPassword: string, newPassword: string, newPasswordRepeat: string): Observable<AppUser> {
    return this.http.put('/api/users/changePassword/'+userId+'/'+oldPassword+'/'+newPassword+'/'+newPasswordRepeat+'', this.options)
      .map(response => new AppUser(response.json()))
      .catch(this.handleError);
  }

  findAllAppUsersWhichDoNotHaveReportWithThisId(id : any) : Observable<AppUser[]> {
    return this.http.get('/api/users/findAllAppUsersWhichDoNotHaveReportWithThisId/' + id)
      .map(response => AppUser.toArray(response.json()))
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
