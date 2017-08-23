import { Injectable } from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {PageRequestByExample, PageResponse} from "../../support/paging";
import {LazyLoadEvent} from "primeng/primeng";
import {MessageService} from "../../service/message.service";
import {Role} from "./role";

@Injectable()
export class RoleDataService {

  private options = new RequestOptions({ headers: new Headers({ 'Content-Type': 'application/json' }) });

  constructor(private http: Http) {}

  /**
   * Get a Role by id.
   */
  getRole(id : any) : Observable<Role> {
    return this.http.get('/api/roles/' + id)
      .map(response => new Role(response.json()))
      .catch(this.handleError);
  }

  /**
   * Update the passed role.
   */
  update(role : Role) : Observable<Role> {
    let body = JSON.stringify(role);

    return this.http.put('/api/roles/', body, this.options)
      .map(response => new Role(response.json()))
      .catch(this.handleError);
  }

  /**
   * Load a page (for paginated datatable) of Role using the passed
   * role as an example for the search by example facility.
   */
  getPage(role : Role, event : LazyLoadEvent) : Observable<PageResponse<Role>> {
    let req = new PageRequestByExample(role, event);
    let body = JSON.stringify(req);

    return this.http.post('/api/roles/page', body, this.options)
      .map(response => {
        let pr : any = response.json();
        return new PageResponse<Role>(pr.totalPages, pr.totalElements, Role.toArray(pr.content));
      })
      .catch(this.handleError);
  }

  /**
   * Find all roles which do not have user with this id.
   */
  findAllRolesWhichDoNotHaveAppUserWithThisId(id : any) : Observable<Role[]> {
    return this.http.get('/api/roles/findAllRolesWhichDoNotHaveAppUserWithThisId/' + id)
      .map(response => Role.toArray(response.json()))
      .catch(this.handleError);
  }

  /**
   * Delete an Role by id.
   */
  delete(id : any) {
    return this.http.delete('/api/roles/' + id).catch(this.handleError);
  }

  // sample method from angular doc
  private handleError (error: any) {
    // TODO: seems we cannot use messageService from here...
    let errMsg = (error.message) ? error.message :
      error.status ? `Status: ${error.status} - Text: ${error.statusText}` : 'Server error';
    console.error(errMsg); // log to console instead
    if (error.status === 401 ) {
      window.location.href = '/';
    }
    return Observable.throw(errMsg);
  }
}
