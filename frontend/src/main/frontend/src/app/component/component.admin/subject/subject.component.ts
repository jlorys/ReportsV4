import {Component, Input, SimpleChanges} from "@angular/core";
import {PageResponse} from "../../../support/paging";
import {Subject} from "./subject";
import {LazyLoadEvent, Message} from "primeng/primeng";
import {Router} from "@angular/router";
import {SubjectDataService} from "./subject.data.service";
import {MdDialog} from "@angular/material";
import {ConfirmDeleteDialogComponent} from "../../../support/confirm-delete-dialog.component";

@Component({
  selector: 'subjects',
  templateUrl: './subject.component.html'
})
export class SubjectComponent {

  @Input() header = "Przedmioty...";
  // list is paginated
  currentPage: PageResponse<Subject> = new PageResponse<Subject>(0, 0, []);
  // basic search criterias
  example: Subject = new Subject();

  msgs: Message[] = [];

  constructor(protected router: Router,
              private subjectDataService: SubjectDataService,
              private confirmDeleteDialog: MdDialog) {
  }

  showDeleteDialog(rowData: any) {
    let subjectToDelete: Subject = <Subject> rowData;

    let dialogRef = this.confirmDeleteDialog.open(ConfirmDeleteDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result === 'delete') {
        this.delete(subjectToDelete);
      }
    });
  }

  private delete(subjectToDelete: Subject) {
    let id = subjectToDelete.id;

    this.msgs = []; //this line fix disappearing of messages
    this.subjectDataService.delete(id).subscribe(
      response => {
        this.currentPage.remove(subjectToDelete);
        this.updateVisibility();
      },
      error => this.msgs.push({severity:'error', summary:'Nie można usunąć!', detail: error})
    );
  }

  ngOnChanges(changes: SimpleChanges) {
    this.loadPage({first: 0, rows: 10, sortField: null, sortOrder: null, filters: null, multiSortMeta: null});
  }

  /**
   * Invoked automatically by primeng datatable.
   */
  loadPage(event: LazyLoadEvent) {
    this.msgs = []; //this line fix disappearing of messages
    this.subjectDataService.getPage(this.example, event).subscribe(
      pageResponse => this.currentPage = pageResponse,
      error => this.msgs.push({severity:'error', summary:'Błąd pobierania danych!', detail: error})
    );
  }

  addNew() {
      this.router.navigate(['/subjects/add']);
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/subjects', id]);
  }

  //This method is for refreshing dataTable
  visible: boolean = true;
  updateVisibility(): void {
    this.visible = false;
    setTimeout(() => this.visible = true, 0);
  }
}
