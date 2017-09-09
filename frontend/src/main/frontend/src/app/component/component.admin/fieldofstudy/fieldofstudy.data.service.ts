import { Injectable } from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {PageRequestByExample, PageResponse} from "../../../support/paging";
import {LazyLoadEvent} from "primeng/primeng";
import {FieldOfStudy} from "./fieldofstudy";

@Injectable()
export class FieldOfStudyDataService {

  private options = new RequestOptions({ headers: new Headers({ 'Content-Type': 'application/json' }) });

  constructor(private http: Http) {}

  /**
   * Delete an FieldOfStudy by id.
   */
  delete(id: any) {
    return this.http.delete('/api/fieldOfStudy/' + id).catch(this.handleError);
  }

  /**
   * Load a page (for paginated datatable) of FieldOfStudy using the passed
   * user as an example for the search by example facility.
   */
  getPage(fieldOfStudy: FieldOfStudy, event: LazyLoadEvent): Observable<PageResponse<FieldOfStudy>> {
    let req = new PageRequestByExample(fieldOfStudy, event);
    let body = JSON.stringify(req);

    return this.http.post('/api/fieldOfStudy/page', body, this.options)
      .map(response => {
        let pr: any = response.json();
        return new PageResponse<FieldOfStudy>(pr.totalPages, pr.totalElements, FieldOfStudy.toArray(pr.content));
      })
      .catch(this.handleError);
  }

  /**
   * Get a FieldOfStudy by id.
   */
  getFieldOfStudy(id: any): Observable<FieldOfStudy> {
    return this.http.get('/api/fieldOfStudy/' + id)
      .map(response => new FieldOfStudy(response.json()))
      .catch(this.handleError);
  }

  /**
   * Update the passed fieldOfStudy.
   */
  update(fieldOfStudy: FieldOfStudy): Observable<FieldOfStudy> {
    let body = JSON.stringify(fieldOfStudy);

    return this.http.put('/api/fieldOfStudy/', body, this.options)
      .map(response => new FieldOfStudy(response.json()))
      .catch(this.handleError);
  }

  /**
   * Find all FieldsOfStudies.
   */
  findAll() : Observable<FieldOfStudy[]> {
    return this.http.get('/api/fieldOfStudy/findAll/')
      .map(response => FieldOfStudy.toArray(response.json()))
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
