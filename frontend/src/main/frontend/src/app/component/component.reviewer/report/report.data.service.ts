import { Injectable } from '@angular/core';
import {Http, RequestOptions, Headers, ResponseContentType} from '@angular/http';
import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {PageRequestByExample, PageResponse} from "../../../support/paging";
import {LazyLoadEvent} from "primeng/primeng";
import {Report} from "../../component.admin/report/report";

@Injectable()
export class ReviewerReportDataService {

  private options = new RequestOptions({ headers: new Headers({ 'Content-Type': 'application/json' }) });

  constructor(private http: Http) {}

  getReport(id : any) : Observable<Report> {
    return this.http.get('/api/reports/' + id)
      .map(response => new Report(response.json()))
      .catch(this.handleError);
  }

  update(report : Report) : Observable<Report> {
    let body = JSON.stringify(report);

    return this.http.put('/api/reviewerReports/', body, this.options)
      .map(response => new Report(response.json()))
      .catch(this.handleError);
  }

  downloadFile(id): Observable<Blob> {
    let options = new RequestOptions({responseType: ResponseContentType.Blob });
    return this.http.get('/api/reports/file/' + id, options)
      .map(res => res.blob())
      .catch(this.handleError)
  }

  /**
   * Load a page (for paginated datatable) of Report using the passed
   * Report as an example for the search by example facility.
   */
  getPage(report : Report, event : LazyLoadEvent) : Observable<PageResponse<Report>> {
    let req = new PageRequestByExample(report, event);
    let body = JSON.stringify(req);

    return this.http.post('/api/reports/page', body, this.options)
      .map(response => {
        let pr : any = response.json();
        return new PageResponse<Report>(pr.totalPages, pr.totalElements, Report.toArray(pr.content));
      })
      .catch(this.handleError);
  }

  // sample method from angular doc
  private handleError (error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `Status: ${error.status} - Text: ${error.statusText}` : 'Server error';
    console.error(errMsg); // log to console instead
    if (error.status === 401 ) {
      window.location.href = '/';
    }
    return Observable.throw(errMsg);
  }
}
