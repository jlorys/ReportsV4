import { Injectable } from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {PageRequestByExample, PageResponse} from "../../support/paging";
import {LazyLoadEvent} from "primeng/primeng";
import {Laboratory} from "./laboratory";

@Injectable()
export class LaboratoryDataService {

  private options = new RequestOptions({ headers: new Headers({ 'Content-Type': 'application/json' }) });

  constructor(private http: Http) {}

  /**
   * Delete an Laboratory by id.
   */
  delete(id: any) {
    return this.http.delete('/api/laboratory/' + id).catch(this.handleError);
  }

  /**
   * Load a page (for paginated datatable) of Laboratory using the passed
   * user as an example for the search by example facility.
   */
  getPage(laboratory: Laboratory, event: LazyLoadEvent): Observable<PageResponse<Laboratory>> {
    let req = new PageRequestByExample(laboratory, event);
    let body = JSON.stringify(req);

    return this.http.post('/api/laboratory/page', body, this.options)
      .map(response => {
        let pr: any = response.json();
        return new PageResponse<Laboratory>(pr.totalPages, pr.totalElements, Laboratory.toArray(pr.content));
      })
      .catch(this.handleError);
  }

  /**
   * Get a Laboratory by id.
   */
  getLaboratory(id: any): Observable<Laboratory> {
    return this.http.get('/api/laboratory/' + id)
      .map(response => new Laboratory(response.json()))
      .catch(this.handleError);
  }

  /**
   * Update the passed Laboratory.
   */
  update(laboratory: Laboratory): Observable<Laboratory> {
    let body = JSON.stringify(laboratory);

    return this.http.put('/api/laboratory/', body, this.options)
      .map(response => new Laboratory(response.json()))
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
