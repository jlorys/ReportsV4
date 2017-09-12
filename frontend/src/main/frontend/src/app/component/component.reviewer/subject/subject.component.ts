import {Component} from "@angular/core";
import {SubjectComponent} from "../../component.admin/subject/subject.component";

@Component({
  selector: 'reviewerSubjects',
  templateUrl: '../../component.admin/subject/subject.component.html'
})
export class ReviewerSubjectComponent extends SubjectComponent{

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reviewerSubjects', id]);
  }
}
